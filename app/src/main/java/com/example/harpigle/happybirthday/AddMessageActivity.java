package com.example.harpigle.happybirthday;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddMessageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText message;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);

        findViews();
        setActionBar();
        setMessageEntryMaxHeight();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessagesSharedPrefs messagesSharedPrefs =
                        MessagesSharedPrefs.getInstance(AddMessageActivity.this);
                BirthdayUtility birthdayUtility = new BirthdayUtility();
                String messageString = message.getText().toString();

                // Message string validation
                if (messageString.equals("")) {
                    Toast.makeText(
                            AddMessageActivity.this,
                            getString(R.string.message_not_entered),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                } else if (messageString.length() < 10) {
                    Toast.makeText(
                            AddMessageActivity.this,
                            getString(R.string.message_characters_not_enough),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                // Check for existence, then put to the shared prefs
                if (!birthdayUtility.isMessageExited(messagesSharedPrefs, messageString)) {
                    if (messagesSharedPrefs.put(messageString)) {
                        Toast.makeText(
                                AddMessageActivity.this,
                                getString(R.string.message_registered),
                                Toast.LENGTH_SHORT
                        ).show();
                        message.setText("");
                    } else {
                        Toast.makeText(
                                AddMessageActivity.this,
                                getString(R.string.error_occurred),
                                Toast.LENGTH_SHORT
                        ).show();
                        message.setText("");
                    }
                } else
                    Toast.makeText(
                            AddMessageActivity.this,
                            getString(R.string.message_exists),
                            Toast.LENGTH_SHORT
                    ).show();
            }
        });
    }

    private void findViews() {
        toolbar = findViewById(R.id.add_message_toolbar);
        message = findViewById(R.id.message_context_add_message);
        registerBtn = findViewById(R.id.register_message_add_message);
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.add_message_main_menu));
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else
            Log.e("ActionBar", "Register activity action bar is null");
    }

    private void setMessageEntryMaxHeight() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        message.setMaxHeight(displayMetrics.heightPixels / 2);
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
