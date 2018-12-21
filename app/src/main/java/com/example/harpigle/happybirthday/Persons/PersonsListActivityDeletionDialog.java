package com.example.harpigle.happybirthday.Persons;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.harpigle.happybirthday.R;

public class PersonsListActivityDeletionDialog extends DialogFragment {

    private String name;

    public PersonsListActivityDeletionDialog() {

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

        /*
        If the name is null, it means that the user tapped the clear action in action bar;
        else it means that the user wants to delete a certain person
        */
        if (name == null) {
            prompt.setMessage(getString(R.string.persons_clear_prompt_message_persons_list));
            prompt.setPositiveButton(getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PersonsSharedPrefs sharedPref =
                                    PersonsSharedPrefs.getInstance(getContext());
                            if (sharedPref.clear()) {
                                Toast.makeText(
                                        getActivity(),
                                        getString(R.string.successfully_cleared_persons_list),
                                        Toast.LENGTH_SHORT
                                ).show();
                                listener.deletionDone();
                            }
                        }
                    });
            prompt.setNegativeButton(getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    });

            return prompt.create();
        } else {
            prompt.setMessage(getString(R.string.person_deletion_prompt_message_persons_list, name));
            prompt.setPositiveButton(getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PersonsSharedPrefs personsSharedPrefs =
                                    PersonsSharedPrefs.getInstance();
                            if (personsSharedPrefs.remove(name)) {
                                Toast.makeText(
                                        getActivity(),
                                        getString(
                                                R.string.person_deletion_successful_persons_list,
                                                name
                                        ),
                                        Toast.LENGTH_SHORT
                                ).show();
                                listener.deletionDone();
                            } else
                                Toast.makeText(
                                        getActivity(),
                                        getString(R.string.person_deletion_failed_persons_list),
                                        Toast.LENGTH_SHORT
                                ).show();
                        }
                    });
            prompt.setNegativeButton(getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    });

            return prompt.create();
        }
    }

    private void getArgs() {
        Bundle bundle = getArguments();
        this.name = bundle != null ? bundle.getString("name") : null;
    }
}