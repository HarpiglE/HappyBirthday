package com.example.harpigle.happybirthday;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonArray != null) {
            editor.putString(key, jsonArray.toString());
            editor.apply();
            return true;
        } else
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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonArray;
        }

    }

    public String[] getKeys() {
        Map<String, ?> allPrefs = sharedPreferences.getAll();

        String[] keysArray = new String[allPrefs.size()];
        allPrefs.keySet().toArray(keysArray);

        return keysArray;
    }

    public ArrayList<JSONArray> getValues() {
        Map<String, ?> allPrefs = sharedPreferences.getAll();

        ArrayList<JSONArray> jsonArrays = new ArrayList<>();

        String[] stringsToJsonArrays = new String[allPrefs.size()];
        allPrefs.values().toArray(stringsToJsonArrays);

        for (int i = 0; i < allPrefs.size(); i++) {
            JSONArray jsonArray = null;

            try {
                jsonArray = new JSONArray(stringsToJsonArrays[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonArrays.add(jsonArray);
        }

        return jsonArrays;
    }
}
