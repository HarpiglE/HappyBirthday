package com.example.harpigle.happybirthday.Persons;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.harpigle.happybirthday.BirthdayUtility;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public final class PersonsSharedPrefs {

    private static final String PERSONS_SHARED_PREF = "Persons";
    private static PersonsSharedPrefs instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private PersonsSharedPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(
                PERSONS_SHARED_PREF, Context.MODE_PRIVATE
        );
        editor = sharedPreferences.edit();
    }

    public static PersonsSharedPrefs getInstance(Context context) {
        if (instance == null) {
            instance = new PersonsSharedPrefs(context);
        }
        return instance;
    }

    public static PersonsSharedPrefs getInstance() {
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

    public ArrayList<String[]> getValues() {
        Map<String, ?> allPrefs = sharedPreferences.getAll();

        String[] stringsToJsonArrays = new String[allPrefs.size()];
        allPrefs.values().toArray(stringsToJsonArrays);

        ArrayList<String[]> ListsArray = new ArrayList<>();
        BirthdayUtility utility = new BirthdayUtility();

        /*
        Get encoded name, date, time and phone number respectively;
        decode them and store in the valuesList
        */
        for (int i = 0; i < allPrefs.size(); i++) {
            JSONArray jsonArray;
            String[] valuesList = new String[4];

            try {
                jsonArray = new JSONArray(stringsToJsonArrays[i]);

                valuesList[0] = utility.decodeIt(jsonArray.get(0).toString());
                valuesList[1] = utility.decodeIt(jsonArray.get(1).toString());
                valuesList[2] = utility.decodeIt(jsonArray.get(2).toString());
                valuesList[3] = "0" + utility.decodeIt(jsonArray.get(3).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ListsArray.add(valuesList);
        }

        return ListsArray;
    }

    public boolean remove(String plainName) {
        String[] keys = this.getKeys();

        // Encode name to compare with keys in shared prefs
        try {
            plainName = URLEncoder.encode(plainName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Look for a key that contains the given name and remove it
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].contains(plainName)) {
                editor.remove(keys[i]);
                editor.apply();
                return true;
            }
        }

        return false;
    }

    public boolean clear() {
        editor.clear();
        editor.apply();
        return true;
    }
}