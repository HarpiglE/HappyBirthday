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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class InformationActivity extends AppCompatActivity
        implements InformationActivityDeletionDialog.DeletionDialogListener {

    private RecyclerView recyclerView;
    private InformationActivityAdapter adapter;
    private InformationActivityItemClickListener itemClickListener;

    private Toolbar toolbar;
    private ImageView warningLogo;
    private TextView warningText;

    private ArrayList<String[]> information = new ArrayList<>();

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
                    @Nullable String time,
                    @Nullable String phoneNumber
            ) {
                InformationActivityDeletionDialog dialog = new InformationActivityDeletionDialog();

                Bundle info = new Bundle();
                info.putString("name", name);

                if (date == null || time == null || phoneNumber == null) {
                    dialog.setArguments(info);
                    dialog.show(getSupportFragmentManager(), "DELETION_DIALOG");
                } else {
                    info.putString("date", date);
                    info.putString("time", time);
                    info.putString("phone_number", phoneNumber);

                    Intent informationActivityEditionDialog = new Intent(
                            InformationActivity.this,
                            InformationActivityEditionDialog.class
                    );
                    informationActivityEditionDialog.putExtra("information", info);
                    startActivityForResult(informationActivityEditionDialog, 0);
                }
            }
        };

        setUpRecyclerView();
    }

    private void findViews() {
        recyclerView = findViewById(R.id.information_recycler_view);
        toolbar = findViewById(R.id.information_toolbar);
        warningLogo = findViewById(R.id.warning_logo_information);
        warningText = findViewById(R.id.warning_text_information);
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

        return birthDaySharedPref.getValues();
    }

    private void setUpRecyclerView() {
        information = extractInformation();

        // Show warning logo and text to notify to user that there's no data to show
        showWarning(information.size());

        adapter = new InformationActivityAdapter(information, itemClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));
        recyclerView.setAdapter(adapter);
    }

    private void showWarning(int size) {
        if (size == 0) {
            warningLogo.setVisibility(View.VISIBLE);
            warningText.setVisibility(View.VISIBLE);
        }
    }

    // Deletion dialog callback
    @Override
    public void deletionDone() {
        setUpRecyclerView();
    }

    // Edition dialog callback
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK)
            setUpRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.information_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.clear_information:
                if (information.size() > 0) {
                    InformationActivityDeletionDialog clearDialog =
                            new InformationActivityDeletionDialog();
                    clearDialog.show(getSupportFragmentManager(), "CLEAR_DIALOG");
                }
        }
        return super.onOptionsItemSelected(item);
    }
}