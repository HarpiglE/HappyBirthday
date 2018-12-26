package com.example.harpigle.happybirthday;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.harpigle.happybirthday.Messages.Activity.AddMessageActivity;
import com.example.harpigle.happybirthday.Messages.Activity.MessagesListActivity;
import com.example.harpigle.happybirthday.Persons.Activity.AddPersonActivity;
import com.example.harpigle.happybirthday.Persons.Activity.PersonsListActivity;

public class MainMenuActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ConstraintLayout addPerson;
    private ConstraintLayout addMessage;
    private ConstraintLayout personsList;
    private ConstraintLayout messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        findViews();

        setSupportActionBar(toolbar);

        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(MainMenuActivity.this, AddPersonActivity.class);
                startActivity(registerActivity);
            }
        });
        personsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent informationActivity = new Intent(MainMenuActivity.this, PersonsListActivity.class);
                startActivity(informationActivity);
            }
        });
        addMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addMessageActivity = new Intent(MainMenuActivity.this, AddMessageActivity.class);
                startActivity(addMessageActivity);
            }
        });
        messagesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messagesListActivity = new Intent(MainMenuActivity.this, MessagesListActivity.class);
                startActivity(messagesListActivity);
            }
        });
    }

    private void findViews() {
        toolbar = findViewById(R.id.toolbar_main_menu);
        addPerson = findViewById(R.id.add_person_main_menu);
        addMessage = findViewById(R.id.add_message_main_menu);
        personsList = findViewById(R.id.persons_list_main_menu);
        messagesList = findViewById(R.id.messages_list_main_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting_popup_main_menu:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
