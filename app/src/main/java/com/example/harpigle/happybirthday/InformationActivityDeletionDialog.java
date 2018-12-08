package com.example.harpigle.happybirthday;

import android.app.Dialog;
import android.content.Context;
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

    private String name;

    public InformationActivityDeletionDialog() {

    }

    public interface DeletionDialogListener {
        void deletionDone();
    }

    private DeletionDialogListener listener;

    // Verify that the host activity implements the callback interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DeletionDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement DeletionDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        getArgs();

        AlertDialog.Builder prompt =
                new AlertDialog.Builder(getActivity());

        prompt.setMessage(getString(R.string.deletion_prompt_message, name));
        prompt.setPositiveButton(getString(R.string.deletion_prompt_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BirthDaySharedPref birthDaySharedPref = BirthDaySharedPref.getInstance();
                        if (birthDaySharedPref.remove(name)) {
                            Toast.makeText(
                                    getActivity(),
                                    getString(R.string.deletion_successful, name),
                                    Toast.LENGTH_SHORT
                            ).show();
                            listener.deletionDone();
                        } else
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
    }

    private void getArgs() {
        Bundle bundle = getArguments();
        this.name = bundle.getString("name");
    }
}