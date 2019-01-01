package com.example.harpigle.happybirthday.Persons;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harpigle.happybirthday.R;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

public class PersonsListActivityEditionDialog extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private String name;
    private String date;
    private String time;
    private String number;

    private EditText nameEdt;
    private EditText numberEdt;
    private Button dateBtn;
    private Button timeBtn;
    private TextView dateHint;
    private TextView timeHint;
    private Button conform;
    private Button cancel;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* For hiding a title bar in the activity-dialog that appears and that uses up the little
           available space. For those who are not using AppCompatActivity:
           requestWindowFeature(Window.FEATURE_NO_TITLE);
          */
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_persons_list_edition);

        getInformation();
        findViews();
        setTextsIntoViews();
        configureButtons();
    }

    private void getInformation() {
        Bundle info = getIntent().getBundleExtra("information");

        name = info.getString("name");
        number = info.getString("number");
        date = info.getString("date");
        time = info.getString("time");
    }

    private void findViews() {
        nameEdt = findViewById(R.id.name_edt_dialog);
        numberEdt = findViewById(R.id.phone_number_edt_dialog);
        dateBtn = findViewById(R.id.birth_date_picker_dialog);
        timeBtn = findViewById(R.id.birth_time_picker_dialog);
        dateHint = findViewById(R.id.date_show_tv_dialog);
        timeHint = findViewById(R.id.time_show_tv_dialog);
        conform = findViewById(R.id.conform_dialog);
        cancel = findViewById(R.id.cancel_dialog);
    }

    private void setTextsIntoViews() {
        nameEdt.setText(name);
        numberEdt.setText(number);
        dateHint.setText(date);
        timeHint.setText(time);
    }

    private void configureButtons() {
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateHint.getHint().toString().equals(getString(R.string.wrong_date_add_person))) {
                    dateHint.setHint("");
                    configureDatePicker();
                } else {
                    configureCurrentPickedDateOnPicker();
                }
            }
        });
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configureCurrentPickedTimeOnPicker();
            }
        });
        conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonsSharedPrefs personsSharedPrefs =
                        PersonsSharedPrefs.getInstance(PersonsListActivityEditionDialog.this);
                String key = personsSharedPrefs.getKey(name);

                name = nameEdt.getText().toString();
                number = numberEdt.getText().toString();
                date = dateHint.getText().toString();
                time = timeHint.getText().toString();

                String[] info = {name, number, date, time};

                // Store the new ones
                if (isInformationTrue() && personsSharedPrefs.putValue(key, info)) {
                    Toast.makeText(
                            PersonsListActivityEditionDialog.this,
                            getString(R.string.person_registered_add_person, name),
                            Toast.LENGTH_SHORT
                    ).show();
                    Intent intent = new Intent();
                    intent.putExtra("isEditionDone", true);
                    setResult(RESULT_OK, intent);
                    finish();

                } else {
                    Toast.makeText(
                            PersonsListActivityEditionDialog.this,
                            getString(R.string.error_occurred),
                            Toast.LENGTH_SHORT
                    ).show();
                    finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    private void configureCurrentPickedDateOnPicker() {
        String newDate = dateHint.getText().toString().replaceAll("/", "");

        year = Integer.valueOf(newDate.substring(0, 4));
        month = Integer.valueOf(newDate.substring(4, 6)) - 1;
        day = Integer.valueOf(newDate.substring(6, 8));

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                year,
                month,
                day
        );
        datePickerDialog.show(getFragmentManager(), getString(R.string.persian_date_picker));
    }

    private void configureCurrentPickedTimeOnPicker() {
        String newTime = timeHint.getText().toString().replaceAll(":", "");

        hour = Integer.valueOf(newTime.substring(0, 2));
        minute = Integer.valueOf(newTime.substring(2, 4));

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

        if (name.equals("")) {
            Toast.makeText(
                    this,
                    getString(R.string.name_not_entered_add_person),
                    Toast.LENGTH_SHORT
            ).show();
            truthFlag = false;
        }
        if (!number.matches("^09[0-9]{9}$")) {
            Toast.makeText(
                    this,
                    getString(R.string.enter_valid_phone_number_add_person),
                    Toast.LENGTH_SHORT
            ).show();
            truthFlag = false;
        }

        return truthFlag;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        ++monthOfYear;

        PersianCalendar persianCalendar = new PersianCalendar();
        if (year == persianCalendar.getPersianYear()
                && monthOfYear >= persianCalendar.getPersianMonth()
                && dayOfMonth > persianCalendar.getPersianDay()) {

            dateHint.setText("");
            dateHint.setHint(getString(R.string.wrong_date_add_person));

        } else if (year > persianCalendar.getPersianYear()) {
            dateHint.setText("");
            dateHint.setHint(getString(R.string.wrong_date_add_person));

        } else {
            this.year = year;
            this.month = monthOfYear;
            this.day = dayOfMonth;

            String properFormattingDateString;

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

            dateHint.setText(properFormattingDateString);
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;

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

        timeHint.setText(properFormattingTimeString);
    }
}
