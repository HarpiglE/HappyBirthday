package com.example.harpigle.happybirthday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.Calendar;
import java.util.Locale;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
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
