package com.example.harpigle.happybirthday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Toolbar toolbar;
    private EditText nameEdt;
    private EditText phoneNumberEdt;
    private Button datePicker;
    private Button timePicker;
    private TextView dateShow;
    private TextView timeShow;
    private Button registerBtn;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private String nameString;
    private String phoneNumberString;
    private String identifierDate;
    private String properFormattingDateString;
    private String properFormattingTimeString;
    private String encodedNameString;

    private EncodeDecodeString encoding = new EncodeDecodeString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViews();
        setActionBar();
        setButtonsListener();
    }

    private void findViews() {
        toolbar = findViewById(R.id.toolbar_register);
        nameEdt = findViewById(R.id.name_register);
        phoneNumberEdt = findViewById(R.id.phone_number_register);
        datePicker = findViewById(R.id.birth_date_picker);
        timePicker = findViewById(R.id.birth_time_picker);
        dateShow = findViewById(R.id.date_show_tv);
        timeShow = findViewById(R.id.time_show_tv);
        registerBtn = findViewById(R.id.register_btn);
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
    }

    private void setButtonsListener() {
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateShow.getHint().equals(getString(R.string.wrong_date))
                        || dateShow.getHint().equals(getString(R.string.date_not_selected)))
                    configureDatePicker();
                else
                    configureCurrentPickedDateOnPicker();
            }
        });
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeShow.getHint().equals(getString(R.string.time_not_selected)))
                    configureTimePicker();
                else
                    configureCurrentPickedTimeOnPicker();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameString = nameEdt.getText().toString();
                phoneNumberString = phoneNumberEdt.getText().toString();

                // Encoded nameString to utf-8 to store properly in shared preferences
                encodedNameString = encoding.encodeIt(nameString);

                if (isInformationTrue()) {
                    if (isPersonExited(encodedNameString))
                        Toast.makeText(
                                RegisterActivity.this,
                                getString(R.string.person_exists),
                                Toast.LENGTH_SHORT
                        ).show();
                    else if (isPhoneNumberExited(phoneNumberString))
                        Toast.makeText(
                                RegisterActivity.this,
                                getString(R.string.phone_number_exists),
                                Toast.LENGTH_SHORT
                        ).show();
                    else {
                        createIdentifierDate();
                        registeringPerson();
                        emptyEverything();
                    }
                }
            }
        });
    }

    private void configureDatePicker() {
        PersianCalendar persianCalendar = new PersianCalendar();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                persianCalendar.getPersianYear(),
                persianCalendar.getPersianMonth(),
                persianCalendar.getPersianDay()
        );
        datePickerDialog.show(getFragmentManager(), getString(R.string.persian_date_picker));
    }

    private void configureTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                this,
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show(getFragmentManager(), getString(R.string.persian_time_picker));
    }

    private void configureCurrentPickedDateOnPicker() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                year,
                month,
                day
        );
        datePickerDialog.show(getFragmentManager(), getString(R.string.persian_date_picker));
    }

    private void configureCurrentPickedTimeOnPicker() {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                this,
                hour,
                minute,
                true
        );
        timePickerDialog.show(getFragmentManager(), getString(R.string.persian_time_picker));
    }

    private boolean isInformationTrue() {
        boolean truthFlag = true;

        if (nameString.equals("")) {
            Toast.makeText(
                    this,
                    getString(R.string.name_not_entered),
                    Toast.LENGTH_SHORT
            ).show();
            truthFlag = false;
        }
        if (!phoneNumberString.matches("^0?9[0-9]{9}$")) {
            Toast.makeText(
                    this,
                    getString(R.string.enter_valid_phone_number),
                    Toast.LENGTH_SHORT
            ).show();
            truthFlag = false;
        }
        if (year == 0) {
            Toast.makeText(
                    this,
                    getString(R.string.date_not_entered),
                    Toast.LENGTH_SHORT
            ).show();
            truthFlag = false;
        }
        if (hour == 0) {
            Toast.makeText(
                    this,
                    getString(R.string.time_not_entered),
                    Toast.LENGTH_SHORT
            ).show();
            truthFlag = false;
        }

        return truthFlag;
    }

    private void createIdentifierDate() {
        /* This variable is important. the program will look at the current system date and compare
           it and will send SMS according to this variable
          */
        identifierDate = String.valueOf(
                String.valueOf(year) + String.valueOf(month) + String.valueOf(day)
        );
    }

    private void registeringPerson() {
        // Register date by 'date_encodedName' pattern
        identifierDate += ("_" + encodedNameString);

        // Encode date and time string to store in the shared prefs
        properFormattingDateString = encoding.encodeIt(properFormattingDateString);
        properFormattingTimeString = encoding.encodeIt(properFormattingTimeString);
        phoneNumberString = encoding.encodeIt(phoneNumberString);

        String[] info =
                {encodedNameString, properFormattingDateString,
                        properFormattingTimeString, phoneNumberString};

        BirthDaySharedPref birthDaySharedPref = BirthDaySharedPref.getInstance();
        if (birthDaySharedPref.put(identifierDate, info)) {
            Toast.makeText(
                    this,
                    getString(R.string.person_registered, nameString),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private boolean isPersonExited(String name) {
        BirthDaySharedPref birthDaySharedPref =
                BirthDaySharedPref.getInstance(RegisterActivity.this);
        String[] keys = birthDaySharedPref.getKeys();

        for (int i = 0; i < keys.length; i++) {
            if (keys[i].contains(name))
                return true;
        }

        return false;
    }

    private boolean isPhoneNumberExited(String phoneNumber) {
        BirthDaySharedPref birthDaySharedPref =
                BirthDaySharedPref.getInstance(RegisterActivity.this);

        if (phoneNumber.charAt(0) == '0')
            phoneNumber = phoneNumber.replaceFirst("0", "");

        ArrayList<String[]> values = birthDaySharedPref.getValues();

        for (int i = 0; i < values.size(); i++) {
            if (values.get(i)[3].equals(phoneNumber))
                return true;
        }

        return false;
    }

    private void emptyEverything() {
        nameEdt.setText("");
        phoneNumberEdt.setText("");
        dateShow.setText("");
        timeShow.setText("");
        dateShow.setHint(getString(R.string.date_not_selected));
        timeShow.setHint(getString(R.string.time_not_selected));

        nameString = "";
        identifierDate = "";
        properFormattingDateString = "";
        properFormattingTimeString = "";
        encodedNameString = "";
        phoneNumberString = "";

        year = 0;
        hour = 0;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        ++monthOfYear;

        PersianCalendar persianCalendar = new PersianCalendar();
        if (year == persianCalendar.getPersianYear()
                && monthOfYear >= persianCalendar.getPersianMonth()
                && dayOfMonth > persianCalendar.getPersianDay()) {

            // Assign zero to year so when user clicked register button, there's no correct date
            this.year = 0;

            dateShow.setText("");
            dateShow.setHint(getString(R.string.wrong_date));

        } else if (year > persianCalendar.getPersianYear()) {

            // Assign zero to year so when user clicked register button, there's no correct date
            this.year = 0;

            dateShow.setText("");
            dateShow.setHint(getString(R.string.wrong_date));

        } else {
            this.year = year;
            this.month = monthOfYear;
            this.day = dayOfMonth;

            // Format date display in text view properly
            properFormattingDateString = year + "/";
            if (monthOfYear < 10)
                properFormattingDateString += String.valueOf("0" + monthOfYear);
            else
                properFormattingDateString += String.valueOf(monthOfYear);
            properFormattingDateString += "/";
            if (dayOfMonth < 10)
                properFormattingDateString += String.valueOf("0" + dayOfMonth);
            else
                properFormattingDateString += String.valueOf(dayOfMonth);

            dateShow.setText(properFormattingDateString);
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;

        // Format time display in text view properly
        if (hourOfDay < 10)
            properFormattingTimeString = String.valueOf("0" + hourOfDay);
        else
            properFormattingTimeString = String.valueOf(hourOfDay);
        properFormattingTimeString += ":";
        if (minute < 10)
            properFormattingTimeString += String.valueOf("0" + minute);
        else
            properFormattingTimeString += String.valueOf(minute);

        timeShow.setText(properFormattingTimeString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.birthday_popup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.show_information_popup:
                Intent birthdayInformationActivity = new Intent(
                        RegisterActivity.this,
                        InformationActivity.class
                );
                startActivity(birthdayInformationActivity);
        }

        return true;
    }
}