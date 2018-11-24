package com.example.harpigle.happybirthday;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;

public final class BirthDaySharedPref {

    private static final String BIRTHDAY_SHARED_PREF = "BirthDay";
    private static BirthDaySharedPref instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private BirthDaySharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(
                BIRTHDAY_SHARED_PREF, Context.MODE_PRIVATE
        );
        editor = sharedPreferences.edit();
    }

    public static BirthDaySharedPref getInstance(Context context) {
        if (instance == null) {
            instance = new BirthDaySharedPref(context);
        }
        return instance;
    }

    public static BirthDaySharedPref getInstance() {
        if (instance != null) {
            return instance;
        }

        throw new IllegalArgumentException(
                "Should use getInstance(Context) at least once before using this method."
        );
    }

    public boolean put(String key, String[] value) {
        JSONArray jsonArray = null;

        // Store key and values to the shared preferences
        try {
            jsonArray = new JSONArray(Arrays.toString(value));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonArray != null) {
            editor.putString(key, jsonArray.toString());
            editor.apply();
            return true;
        }
        else
            return false;
    }

    public JSONArray get(String key) {
        String personInformationJson = sharedPreferences.getString(key, "KeyNotAvailable");

        if (personInformationJson.equals("KeyNotAvailable"))
            return null;
        else {
            JSONArray jsonArray = null;

            try {
                jsonArray = new JSONArray(personInformationJson);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonArray;
        }

    }
}
