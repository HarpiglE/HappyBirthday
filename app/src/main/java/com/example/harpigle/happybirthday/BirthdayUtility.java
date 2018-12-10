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

    public boolean isPersonExited(Context context, String name) {
        BirthDaySharedPref birthDaySharedPref =
                BirthDaySharedPref.getInstance(context);
        String[] keys = birthDaySharedPref.getKeys();

        for (int i = 0; i < keys.length; i++) {
            if (keys[i].contains(name))
                return true;
        }

        return false;
    }

    public boolean isPhoneNumberExited(Context context, String phoneNumber) {
        BirthDaySharedPref birthDaySharedPref =
                BirthDaySharedPref.getInstance(context);

        ArrayList<String[]> values = birthDaySharedPref.getValues();

        for (int i = 0; i < values.size(); i++) {
            if (values.get(i)[3].equals(phoneNumber))
                return true;
        }

        return false;
    }
}
