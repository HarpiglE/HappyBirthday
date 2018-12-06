package com.example.harpigle.happybirthday;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class InformationActivityAlertDialog extends DialogFragment {

    private int type;
    private String date;
    private String name;

    public InformationActivityAlertDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        getArgs();

        AlertDialog.Builder deletionPrompt =
                new AlertDialog.Builder(getActivity());
        deletionPrompt.setMessage(getString(R.string.deletion_prompt_message, name));
        deletionPrompt.setPositiveButton(getString(R.string.deletion_prompt_yes),
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
        deletionPrompt.setNegativeButton(getString(R.string.deletion_prompt_no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return deletionPrompt.create();
    }

    private void getArgs() {
        Bundle bundle = getArguments();

        this.type = bundle.getInt("prompt_type");
        this.date = bundle.getString("date");
        this.name = bundle.getString("name");
    }
}