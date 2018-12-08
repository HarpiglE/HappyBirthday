package com.example.harpigle.happybirthday;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InformationActivityDeletionDialog extends DialogFragment {

    private int type;
    private String name;
    private String date;
    private String time;

    private EditText nameEdt;
    private Button dateBtn;
    private Button timeBtn;
    private TextView dateHint;
    private TextView timeHint;

    public InformationActivityDeletionDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        getArgs();

        AlertDialog.Builder prompt =
                new AlertDialog.Builder(getActivity());

        /* If type equals to 0, the activity inflate deletion prompt;
           else it inflates edition prompt
         */
        if (type == 0) {
            prompt.setMessage(getString(R.string.deletion_prompt_message, name));
            prompt.setPositiveButton(getString(R.string.deletion_prompt_yes),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BirthDaySharedPref birthDaySharedPref = BirthDaySharedPref.getInstance();
                            if (birthDaySharedPref.remove(name))
                                Toast.makeText(
                                        getActivity(),
                                        getString(R.string.deletion_successful, name),
                                        Toast.LENGTH_SHORT
                                ).show();
                            else
                                Toast.makeText(
                                        getActivity(),
                                        getString(R.string.deletion_failed),
                                        Toast.LENGTH_SHORT
                                ).show();
                        }
                    });
            prompt.setNegativeButton(getString(R.string.deletion_prompt_no),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    });

            return prompt.create();
        } else {
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.edition_information_activity, null);

            prompt.setView(view);

            findViews(view);
            setTextsIntoViews();
            configureButtons();

            prompt.setPositiveButton(R.string.edition_conform,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            prompt.setNegativeButton(R.string.edition_cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            return prompt.create();
        }

    }

    private void getArgs() {
        Bundle bundle = getArguments();

        this.type = bundle.getInt("prompt_type");
        this.name = bundle.getString("name");
        this.date = bundle.getString("date");
        this.time = bundle.getString("time");
    }

    private void findViews(View view) {
        nameEdt = view.findViewById(R.id.name_edt_dialog);
        dateBtn = view.findViewById(R.id.birth_date_picker_dialog);
        timeBtn = view.findViewById(R.id.birth_time_picker_dialog);
        dateHint = view.findViewById(R.id.date_show_tv_dialog);
        timeHint = view.findViewById(R.id.time_show_tv_dialog);
    }

    private void setTextsIntoViews() {
        nameEdt.setText(name);
        dateHint.setText(date);
        timeHint.setText(time);
    }

    private void configureButtons() {
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDate = date.replaceAll("/", "");

                int year = Integer.valueOf(newDate.substring(0, 4));
                int month = Integer.valueOf(newDate.substring(4, 6));
                int day = Integer.valueOf(newDate.substring(6, 8));

//                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
//                        getActivity(),
//                        year,
//                        month,
//                        day
//                );
//                datePickerDialog.show(getFragmentManager(), getString(R.string.persian_date_picker));
            }
        });
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}