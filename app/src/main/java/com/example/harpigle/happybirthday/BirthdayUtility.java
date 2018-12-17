package com.example.harpigle.happybirthday;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BirthdayUtility {

    public BirthdayUtility() {

    }

    public String encodeIt(String string) {
        try {
            string = URLEncoder.encode(string, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    public String decodeIt(String string) {
        try {
            string = URLDecoder.decode(string, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    public boolean isPersonExited(Context context, String encodedName) {
        PersonsSharedPrefs personsSharedPrefs =
                PersonsSharedPrefs.getInstance(context);
        String[] keys = personsSharedPrefs.getKeys();

        for (int i = 0; i < keys.length; i++) {
            if (keys[i].contains(encodedName))
                return true;
        }
        return false;
    }

    public boolean isPhoneNumberExited(Context context, String phoneNumber) {
        PersonsSharedPrefs personsSharedPrefs =
                PersonsSharedPrefs.getInstance(context);
        ArrayList<String[]> values = personsSharedPrefs.getValues();

        for (int i = 0; i < values.size(); i++) {
            if (values.get(i)[3].equals(phoneNumber))
                return true;
        }
        return false;
    }

    public boolean isMessageExited(MessagesSharedPrefs messagesSharedPrefs, String plainMessage) {
        String encodedMessage = this.encodeIt(plainMessage);
        String[] values = messagesSharedPrefs.getAll();
        for (String value : values) {
            if (value.equals(encodedMessage)) {
                return true;
            }
        }
        return false;
    }
}
