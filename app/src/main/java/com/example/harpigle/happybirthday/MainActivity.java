package com.example.harpigle.happybirthday;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Toolbar toolbar;
    private EditText nameEdt;
    private Button checkExistence;
    private TextView existenceMessage;
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

    private byte dateIdCounter = 0;
    private byte numberOfPersonExistence = 0;

    private String nameString;
    private String dateString;
    private String timeString;
    private String encodedNameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setActionBar();
        setButtonsListener();
    }

    private void findViews() {
        toolbar = findViewById(R.id.toolbar);
        nameEdt = findViewById(R.id.name_edt);
        checkExistence = findViewById(R.id.existence_check_btn);
        existenceMessage = findViewById(R.id.existence_message_tv);
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
        checkExistence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameString = nameEdt.getText().toString();

                if (nameString.equals("")) {
                    existenceMessage.setText(getString(R.string.name_not_entered));
                } else {
                    // Encoded nameString to utf-8 to store properly in shared preferences
                    encodedNameString = encodeString(nameString);

                    isPersonExited(encodedNameString);

                    if (numberOfPersonExistence > 0) {
                        existenceMessage.setText(getString(
                                R.string.person_exists,
                                numberOfPersonExistence
                        ));
                        registerBtn.setEnabled(false);
                        numberOfPersonExistence = 0;
                    } else {
                        existenceMessage.setText(getString(R.string.person_not_exists));
                        registerBtn.setEnabled(true);
                    }
                }
            }
        });
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
                if (existenceMessage.getText().toString().equals("")) {
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.first_click_check_btn),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    if (isInformationTrue()) {
                        createDateAndTimeString();
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

        if (nameEdt.getText().toString().equals("")) {
            Toast.makeText(
                    this,
                    getString(R.string.name_not_entered),
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

    private void createDateAndTimeString() {
        // These two variables are different from variables in onDateSet and onTimeSet listener
        dateString = String.valueOf(
                String.valueOf(year) + String.valueOf(month) + String.valueOf(day)
        );
        timeString = String.valueOf(String.valueOf(hour) + String.valueOf(minute));
    }

    private void registeringPerson() {
        if (numberOfPersonExistence > 0) {
            Toast.makeText(
                    this,
                    getString(R.string.person_exists, numberOfPersonExistence),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // If there's no name such the given name, then find the proper date id and store it
            findDateId();

            // Register date by 000000_00 pattern to count number of them properly
            dateString += ("_0" + String.valueOf(dateIdCounter));

            String[] nameAndTime = {encodedNameString, timeString};

            BirthDaySharedPref birthDaySharedPref = BirthDaySharedPref.getInstance();
            if (birthDaySharedPref.put(dateString, nameAndTime)) {
                Toast.makeText(
                        this,
                        getString(R.string.person_registered, nameString),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private String encodeString(String string) {
        try {
            string = URLEncoder.encode(string, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    private void isPersonExited(String name) {
        BirthDaySharedPref birthDaySharedPref = BirthDaySharedPref.getInstance(MainActivity.this);

        // Searching whole shared prefs if there is a name equals to specified name
        ArrayList<JSONArray> allPrefs = birthDaySharedPref.getAll();
        for (int i = 0; i < allPrefs.size(); i++) {
            try {
                if (allPrefs.get(i).get(0).equals(name)) {
                    ++numberOfPersonExistence;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void findDateId() {
        BirthDaySharedPref birthDaySharedPref = BirthDaySharedPref.getInstance();
        if (numberOfPersonExistence == 0) {
            String tempDate;
            while (true) {
                ++dateIdCounter;
                tempDate = dateString + "_0" + String.valueOf(dateIdCounter);
                if (birthDaySharedPref.isKeyExited(tempDate)) {
                    continue;
                } else {
                    break;
                }
            }
        }
    }

    private void emptyEverything() {
        nameEdt.setText("");
        existenceMessage.setText("");
        dateShow.setText("");
        timeShow.setText("");
        dateShow.setHint(getString(R.string.date_not_selected));
        timeShow.setHint(getString(R.string.time_not_selected));

        dateIdCounter = 0;
        numberOfPersonExistence = 0;
        nameString = "";
        dateString = "";
        timeString = "";
        encodedNameString = "";

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
            String properFormattingDateString = year + "/";
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
        String properFormattingTimeString;
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
                FragmentTransaction informationFragmentTransaction =
                        getSupportFragmentManager().beginTransaction();

                informationFragmentTransaction
                        .add(R.id.fragment_container, new BirthdayInformationFragment());

                informationFragmentTransaction.addToBackStack(null);
                informationFragmentTransaction.commit();
        }

        return true;
    }
}