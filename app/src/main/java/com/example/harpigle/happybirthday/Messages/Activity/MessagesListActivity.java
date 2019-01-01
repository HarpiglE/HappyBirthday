package com.example.harpigle.happybirthday.Messages.Activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harpigle.happybirthday.DeletionDialog;
import com.example.harpigle.happybirthday.Messages.MessagesListActivityEditionDialog;
import com.example.harpigle.happybirthday.Messages.MessagesListAdapter;
import com.example.harpigle.happybirthday.Messages.MessagesSharedPrefs;
import com.example.harpigle.happybirthday.Persons.PersonsListCounterDrawable;
import com.example.harpigle.happybirthday.R;

import java.util.ArrayList;

public class MessagesListActivity extends AppCompatActivity implements
        DeletionDialog.PersonsDeletionDialogListener,
        MessagesListActivityEditionDialog.MessagesEditionDialogListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ImageView warningLogo;
    private TextView warningText;

    private MessagesListAdapter adapter;
    private MessagesListAdapter.MessagesListItemClickListener etcClickListener;

    private ArrayList<String> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);

        findViews();
        setActionBar();

        etcClickListener =
                new MessagesListAdapter.MessagesListItemClickListener() {
                    @Override
                    public void onClickListener(@NonNull View view, final String message) {
                        final Bundle info = new Bundle();
                        info.putString("message", message);

                        // Show etc image (...) and set it's items listener
                        PopupMenu etcItemMenu = new PopupMenu(MessagesListActivity.this, view);
                        etcItemMenu
                                .getMenuInflater()
                                .inflate(R.menu.item_persons_list_popup, etcItemMenu.getMenu());
                        etcItemMenu.show();

                        etcItemMenu.setOnMenuItemClickListener(
                                new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.edition_item:
                                                MessagesListActivityEditionDialog editionDialog =
                                                        new MessagesListActivityEditionDialog();
                                                editionDialog.setArguments(info);
                                                editionDialog.show(
                                                        getSupportFragmentManager(),
                                                        "MESSAGES_ITEM_EDITION_DIALOG"
                                                );
                                                break;

                                            case R.id.deletion_item:
                                                info.putString("type", "MESSAGES_LIST_ITEM_REMOVE");

                                                DeletionDialog deletionDialog =
                                                        new DeletionDialog();
                                                deletionDialog.setArguments(info);
                                                deletionDialog.show(
                                                        getSupportFragmentManager(),
                                                        "MESSAGES_ITEM_DELETION_DIALOG"
                                                );
                                                break;
                                        }
                                        return true;
                                    }
                                });
                    }
                };

        setRecyclerView();
    }

    private void findViews() {
        toolbar = findViewById(R.id.toolbar_messages_list);
        recyclerView = findViewById(R.id.recycler_view_messages_list);
        warningLogo = findViewById(R.id.warning_logo_messages_list);
        warningText = findViewById(R.id.warning_text_messages_list);
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.messages_list_main_menu));
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else
            Log.e("ActionBar", "Messages list activity action bar is null");
    }

    private void setRecyclerView() {
        messages = extractMessages();

        adapter = new MessagesListAdapter(messages, etcClickListener);

        // Show warning logo and text to notify to user that there's no data to show
        showWarning(messages.size());

        LinearLayoutManager linearLayout = new LinearLayoutManager(
                MessagesListActivity.this,
                RecyclerView.VERTICAL,
                false
        );
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration recyclerViewDivider = new DividerItemDecoration(
                recyclerView.getContext(),
                linearLayout.getOrientation()
        );
        recyclerView.addItemDecoration(recyclerViewDivider);
        recyclerView.setHasFixedSize(true);
    }

    private ArrayList<String> extractMessages() {
        MessagesSharedPrefs messagesSharedPrefs =
                MessagesSharedPrefs.getInstance(MessagesListActivity.this);
        return messagesSharedPrefs.getValues();
    }

    private void showWarning(int size) {
        if (size == 0) {
            warningLogo.setVisibility(View.VISIBLE);
            warningText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_persons_list, menu);
        menu.findItem(R.id.messages_count).setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setCount(MessagesListActivity.this, menu, String.valueOf(messages.size()));
        return super.onPrepareOptionsMenu(menu);
    }

    // Set person count budge
    public void setCount(Context context, Menu menu, String count) {
        MenuItem menuItem = menu.findItem(R.id.messages_count);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
        PersonsListCounterDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.messages_count_element);
        if (reuse != null && reuse instanceof PersonsListCounterDrawable)
            badge = (PersonsListCounterDrawable) reuse;
        else
            badge = new PersonsListCounterDrawable(context);

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.messages_count_element, badge);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.clear_information:
                if (messages.size() > 0) {
                    Bundle info = new Bundle();
                    info.putString("type", "MESSAGES_LIST_CLEAR");

                    DeletionDialog clearDialog =
                            new DeletionDialog();
                    clearDialog.setArguments(info);
                    clearDialog.show(getSupportFragmentManager(), "MESSAGES_CLEAR_DIALOG");
                }
                break;

            case R.id.messages_count:
                if (messages.size() > 0)
                    Toast.makeText(
                            MessagesListActivity.this,
                            getString(R.string.messages_count_message_messages_list, messages.size()),
                            Toast.LENGTH_SHORT
                    ).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void personDeletionDone() {
        // Update action bar
        invalidateOptionsMenu();
        setRecyclerView();
    }

    @Override
    public void messageEditionDone() {
        setRecyclerView();
    }
}