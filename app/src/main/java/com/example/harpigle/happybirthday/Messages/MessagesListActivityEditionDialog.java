package com.example.harpigle.happybirthday.Messages;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harpigle.happybirthday.R;

public class MessagesListActivityEditionDialog extends DialogFragment {

    private String messageString;

    private TextView messagesEdt;

    public interface MessagesEditionDialogListener {
        void messageEditionDone();
    }

    private MessagesEditionDialogListener listener;

    // Verify that the host activity implements the callback interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (MessagesEditionDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement PersonsDeletionDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        getArgs();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_messages_list_edition, null);
        messagesEdt = dialogView.findViewById(R.id.message_edt_dialog);
        setMessageEntryMaxHeight();
        messagesEdt.setText(messageString);

        final MessagesSharedPrefs messagesSharedPrefs =
                MessagesSharedPrefs.getInstance(getContext());
        final String key = messagesSharedPrefs.getKey(messageString);

        dialog.setView(dialogView);
        dialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                messageString = messagesEdt.getText().toString();

                if (messagesSharedPrefs.putValue(key, messageString)) {
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.message_registered_add_message),
                            Toast.LENGTH_SHORT
                    ).show();
                    listener.messageEditionDone();
                } else
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.error_occurred),
                            Toast.LENGTH_SHORT
                    ).show();
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return dialog.create();
    }

    private void getArgs() {
        Bundle bundle = getArguments();
        if (bundle != null)
            this.messageString = bundle.getString("message");
    }

    private void setMessageEntryMaxHeight() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        messagesEdt.setMaxHeight(displayMetrics.heightPixels / 2);
    }
}
