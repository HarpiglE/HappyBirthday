package com.example.harpigle.happybirthday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private EditText nameEdt;
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

    byte dateRegisteringCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        nameEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String name = nameEdt.getText().toString();
            }
        });

        setButtonsListener();
    }

    private void findViews() {
        nameEdt = findViewById(R.id.name_edt);
        datePicker = findViewById(R.id.birth_date_picker);
        timePicker = findViewById(R.id.birth_time_picker);
        dateShow = findViewById(R.id.date_show_tv);
        timeShow = findViewById(R.id.time_show_tv);
        registerBtn = findViewById(R.id.register_btn);
    }

    private void setButtonsListener() {
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateShow.getText() != "")
                    configureCurrentPickedDateOnPicker();
                else
                    configureDatePicker();
            }
        });
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeShow.getText() != "")
                    configureCurrentPickedTimeOnPicker();
                else
                    configureTimePicker();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInformationTrue()) {
                    registeringPerson();
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
                    getString(R.string.time_not_entered),
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

    private void registeringPerson() {
        String nameString = nameEdt.getText().toString();

        // These two variables are different from variables in onDateSet and onTimeSet listener
        String dateString = String.valueOf(
                String.valueOf(year) + String.valueOf(month) + String.valueOf(day)
        );
        String timeString = String.valueOf(String.valueOf(hour) + String.valueOf(minute));

        // Encoded nameString to utf-8 to store properly in shared preferences
        String encodedNameString;
        encodedNameString = encodeString(nameString);

        byte numberOfPersonExistence = isPersonExited(dateString, encodedNameString);
        if (numberOfPersonExistence > 0) {
            Toast.makeText(
                    this,
                    getString(R.string.person_exists, numberOfPersonExistence),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Register date by 000000_00 pattern to count number of them properly
            dateString += ("_0" + String.valueOf(dateRegisteringCounter));
            dateRegisteringCounter = 0;

            String[] nameAndTime = {encodedNameString, timeString};

            BirthDaySharedPref birthDaySharedPref = BirthDaySharedPref.getInstance();
            if (birthDaySharedPref.put(dateString, nameAndTime)) {
                Toast.makeText(
                        this,
                        getString(R.string.person_registered, nameString),
                        Toast.LENGTH_SHORT
                ).show();
            }

            emptyFields();
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

    private byte isPersonExited(String date, String name) {
        BirthDaySharedPref birthDaySharedPref = BirthDaySharedPref.getInstance(MainActivity.this);

        // Searching whole shared prefs if there is a name equals to specified name
        byte givenNameExitedCounts = 0;
        ArrayList<JSONArray> allPrefs = birthDaySharedPref.getAll();
        for (int i = 0; i < allPrefs.size(); i++) {
            try {
                if (allPrefs.get(i).get(0).equals(name)) {
                    ++givenNameExitedCounts;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // If there's no name such the given name, then find the proper date id and store it
        if (givenNameExitedCounts == 0) {
            String tempDate;
            while (true) {
                ++dateRegisteringCounter;
                tempDate = date + "_0" + String.valueOf(dateRegisteringCounter);
                if (birthDaySharedPref.isKeyExited(tempDate)) {
                    continue;
                } else {
                    break;
                }
            }
        }

        return givenNameExitedCounts;
    }

    private void emptyFields() {
        nameEdt.setText("");
        dateShow.setText("");
        timeShow.setText("");
        dateShow.setHint(getString(R.string.date_not_entered));
        timeShow.setHint(getString(R.string.time_not_entered));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;

        // Format date display in text view properly
        String dateString = year + "/";
        if (monthOfYear < 10)
            dateString += String.valueOf("0" + monthOfYear);
        else
            dateString += String.valueOf(monthOfYear);
        dateString += "/";
        if (dayOfMonth < 10)
            dateString += String.valueOf("0" + dayOfMonth);
        else
            dateString += String.valueOf(dayOfMonth);

        dateShow.setText(dateString);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;

        // Format time display in text view properly
        String timeString;
        if (hourOfDay < 10)
            timeString = String.valueOf("0" + hourOfDay);
        else
            timeString = String.valueOf(hourOfDay);
        timeString += ":";
        if (minute < 10)
            timeString += String.valueOf("0" + minute);
        else
            timeString += String.valueOf(minute);

        timeShow.setText(timeString);
    }
}