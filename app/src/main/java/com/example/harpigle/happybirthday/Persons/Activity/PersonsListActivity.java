package com.example.harpigle.happybirthday.Persons.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harpigle.happybirthday.Persons.PersonsListActivityAdapter;
import com.example.harpigle.happybirthday.Persons.PersonsListActivityDeletionDialog;
import com.example.harpigle.happybirthday.Persons.PersonsListActivityEditionDialog;
import com.example.harpigle.happybirthday.Persons.PersonsListActivityItemClickListener;
import com.example.harpigle.happybirthday.Persons.PersonsListCounterDrawable;
import com.example.harpigle.happybirthday.Persons.PersonsSharedPrefs;
import com.example.harpigle.happybirthday.R;

import java.util.ArrayList;

public class PersonsListActivity extends AppCompatActivity
        implements PersonsListActivityDeletionDialog.DeletionDialogListener {

    private RecyclerView recyclerView;
    private PersonsListActivityAdapter adapter;
    private PersonsListActivityItemClickListener etcClickListener;

    private Toolbar toolbar;
    private ImageView warningLogo;
    private TextView warningText;

    private ArrayList<String[]> information = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons_list);

        findViews();
        configureActionBar();

        etcClickListener = new PersonsListActivityItemClickListener() {
            @Override
            public void onClickListener(
                    @NonNull View view,
                    @NonNull String name,
                    @NonNull final String date,
                    @NonNull final String time,
                    @NonNull final String phoneNumber
            ) {
                final Bundle info = new Bundle();
                info.putString("name", name);

                // Show etc image (...) and set it's items listener
                PopupMenu etcItemMenu = new PopupMenu(PersonsListActivity.this, view);
                etcItemMenu.getMenuInflater()
                        .inflate(R.menu.item_persons_list_popup, etcItemMenu.getMenu());
                etcItemMenu.show();
                etcItemMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.edition_item:
                                info.putString("date", date);
                                info.putString("time", time);
                                info.putString("phone_number", phoneNumber);

                                Intent informationActivityEditionDialog = new Intent(
                                        PersonsListActivity.this,
                                        PersonsListActivityEditionDialog.class
                                );
                                informationActivityEditionDialog.putExtra(
                                        "information",
                                        info
                                );
                                startActivityForResult(informationActivityEditionDialog, 0);
                                break;

                            case R.id.deletion_item:
                                PersonsListActivityDeletionDialog dialog =
                                        new PersonsListActivityDeletionDialog();
                                dialog.setArguments(info);
                                dialog.show(getSupportFragmentManager(), "DELETION_DIALOG");
                                break;
                        }
                        return true;
                    }
                });
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
            actionBar.setTitle(getString(R.string.persons_list_main_menu));
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else
            Log.e("ActionBar", "Information activity action bar is null");
    }

    private ArrayList<String[]> extractInformation() {
        PersonsSharedPrefs personsSharedPrefs =
                PersonsSharedPrefs.getInstance(PersonsListActivity.this);

        return personsSharedPrefs.getValues();
    }

    private void setUpRecyclerView() {
        information = extractInformation();

        // Show warning logo and text to notify to user that there's no data to show
        showWarning(information.size());

        adapter = new PersonsListActivityAdapter(information, etcClickListener);
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
        // Update action bar
        invalidateOptionsMenu();

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
        getMenuInflater().inflate(R.menu.activity_persons_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setCount(PersonsListActivity.this, menu, String.valueOf(information.size()));
        return super.onPrepareOptionsMenu(menu);
    }

    // Set person count budge
    public void setCount(Context context, Menu menu, String count) {
        MenuItem menuItem = menu.findItem(R.id.persons_count);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
        PersonsListCounterDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.persons_count_element);
        if (reuse != null && reuse instanceof PersonsListCounterDrawable) {
            badge = (PersonsListCounterDrawable) reuse;
        } else {
            badge = new PersonsListCounterDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.persons_count_element, badge);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.clear_information:
                if (information.size() > 0) {
                    PersonsListActivityDeletionDialog clearDialog =
                            new PersonsListActivityDeletionDialog();
                    clearDialog.show(getSupportFragmentManager(), "CLEAR_DIALOG");
                }
                break;
            case R.id.persons_count:
                if (information.size() > 0) {
                    Toast.makeText(
                            PersonsListActivity.this,
                            getString(R.string.persons_count_message_persons_list, information.size()),
                            Toast.LENGTH_SHORT
                    ).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}