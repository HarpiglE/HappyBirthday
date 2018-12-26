package com.example.harpigle.happybirthday;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.harpigle.happybirthday.Messages.MessagesSharedPrefs;
import com.example.harpigle.happybirthday.Persons.PersonsSharedPrefs;

public class DeletionDialog extends DialogFragment {

    private String type;
    private String name;
    private String message;

    public interface PersonsDeletionDialogListener {
        void personDeletionDone();
    }

    private PersonsDeletionDialogListener listener;

    // Verify that the host activity implements the callback interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PersonsDeletionDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement PersonsDeletionDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        getArgs();

        final PersonsSharedPrefs personsSharedPrefs = PersonsSharedPrefs.getInstance(getContext());
        final MessagesSharedPrefs messagesSharedPrefs = MessagesSharedPrefs.getInstance(getContext());

        AlertDialog.Builder prompt =
                new AlertDialog.Builder(getActivity());
        prompt.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        switch (type) {

            case "PERSONS_LIST_CLEAR":
                prompt.setMessage(getString(R.string.persons_clear_prompt_message_persons_list));
                prompt.setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (personsSharedPrefs.clear()) {
                                    Toast.makeText(
                                            getActivity(),
                                            getString(R.string.successfully_cleared_persons_list),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    listener.personDeletionDone();
                                } else
                                    Toast.makeText(
                                            getActivity(),
                                            getString(R.string.deletion_failed),
                                            Toast.LENGTH_SHORT
                                    ).show();
                            }
                        });
                return prompt.create();

            case "PERSONS_LIST_ITEM_REMOVE":
                prompt.setMessage(getString(R.string.person_deletion_prompt_message_persons_list, name));
                prompt.setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (personsSharedPrefs.remove(name)) {
                                    Toast.makeText(
                                            getActivity(),
                                            getString(
                                                    R.string.person_deletion_successful_persons_list,
                                                    name
                                            ),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    listener.personDeletionDone();
                                } else
                                    Toast.makeText(
                                            getActivity(),
                                            getString(R.string.deletion_failed),
                                            Toast.LENGTH_SHORT
                                    ).show();
                            }
                        });
                return prompt.create();

            case "MESSAGES_LIST_CLEAR":
                prompt.setMessage(getString(R.string.messages_clear_prompt_message_messages_list));
                prompt.setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (messagesSharedPrefs.clear()) {
                                    Toast.makeText(
                                            getActivity(),
                                            getString(R.string.successfully_cleared_messages_list),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    listener.personDeletionDone();
                                } else
                                    Toast.makeText(
                                            getActivity(),
                                            getString(R.string.deletion_failed),
                                            Toast.LENGTH_SHORT
                                    ).show();
                            }
                        });
                return prompt.create();

            case "MESSAGES_LIST_ITEM_REMOVE":
                prompt.setMessage(getString(
                        R.string.message_deletion_prompt_message_messages_list
                ));
                prompt.setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (messagesSharedPrefs.remove(message)) {
                                    Toast.makeText(
                                            getActivity(),
                                            getString(
                                                    R.string.message_deletion_successful_messages_list
                                            ),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    listener.personDeletionDone();
                                } else
                                    Toast.makeText(
                                            getActivity(),
                                            getString(R.string.deletion_failed),
                                            Toast.LENGTH_SHORT
                                    ).show();
                            }
                        });
                return prompt.create();
        }
        return super.onCreateDialog(savedInstanceState);
    }

    private void getArgs() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.type = bundle.getString("type");

            if (this.type.equals("PERSONS_LIST_ITEM_REMOVE"))
                this.name = bundle.getString("name");

            else if (this.type.equals("MESSAGES_LIST_ITEM_REMOVE"))
                this.message = bundle.getString("message");
        }
    }
}