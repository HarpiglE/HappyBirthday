package com.example.harpigle.happybirthday;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class InformationActivity extends AppCompatActivity
        implements InformationActivityDeletionDialog.DeletionDialogListener {

    private RecyclerView recyclerView;
    private InformationActivityAdapter adapter;
    private InformationActivityItemClickListener itemClickListener;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        findViews();
        configureActionBar();

        itemClickListener = new InformationActivityItemClickListener() {
            @Override
            public void onClickListener(
                    @NonNull String name,
                    @Nullable String date,
                    @Nullable String time
            ) {
                InformationActivityDeletionDialog dialog = new InformationActivityDeletionDialog();

                Bundle info = new Bundle();
                info.putString("name", name);

                if (date == null && time == null) {
                    dialog.setArguments(info);
                    dialog.show(getSupportFragmentManager(), "DELETION_DIALOG");
                } else {
                    info.putString("date", date);
                    info.putString("time", time);

                    Intent informationActivityEditionDialog = new Intent(
                            InformationActivity.this,
                            InformationActivityEditionDialog.class
                    );
                    informationActivityEditionDialog.putExtra("information", info);
                    startActivity(informationActivityEditionDialog);
                }
            }
        };

        setUpRecyclerView();
    }

    private void findViews() {
        recyclerView = findViewById(R.id.information_recycler_view);
        toolbar = findViewById(R.id.information_toolbar);
    }

    private void configureActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("اطلاعات افراد");
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else
            Log.e("ActionBar", "Information activity action bar is null");
    }

    private ArrayList<String[]> extractInformation() {
        BirthDaySharedPref birthDaySharedPref =
                BirthDaySharedPref.getInstance(InformationActivity.this);

        ArrayList<JSONArray> sharedPrefsList = birthDaySharedPref.getValues();
        ArrayList<String[]> adapterList = new ArrayList<>();

        // Get data from shared prefs and store to a array list that contains static string arrays
        for (int i = 0; i < sharedPrefsList.size(); i++) {
            String[] valuesList = new String[3];

            try {
                /* Get encoded name, date and time respectively;
                   decode them and store in the valuesList
                 */
                valuesList[0] = decodeString(sharedPrefsList.get(i).get(0).toString());
                valuesList[1] = decodeString(sharedPrefsList.get(i).get(1).toString());
                valuesList[2] = decodeString(sharedPrefsList.get(i).get(2).toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapterList.add(valuesList);
        }

        return adapterList;
    }

    String decodeString(String string) {
        String decodedString = "";
        try {
            decodedString = URLDecoder.decode(string, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedString;
    }

    private void setUpRecyclerView() {
        ArrayList<String[]> information = extractInformation();

        adapter = new InformationActivityAdapter(information, itemClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));
        recyclerView.setAdapter(adapter);
    }

    // Deletion dialog callback
    @Override
    public void deletionDone() {
        setUpRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}