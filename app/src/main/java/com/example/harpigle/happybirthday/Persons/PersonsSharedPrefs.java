package com.example.harpigle.happybirthday.Persons;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.harpigle.happybirthday.BirthdayUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class PersonsSharedPrefs {

    private static final String PERSONS_SHARED_PREF = "Persons";
    private static PersonsSharedPrefs instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private BirthdayUtility utility = new BirthdayUtility();
    private HashMap<String, String[]> keyAndPersons = new HashMap<>();

    private PersonsSharedPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(
                PERSONS_SHARED_PREF, Context.MODE_PRIVATE
        );
        editor = sharedPreferences.edit();
    }

    public static PersonsSharedPrefs getInstance(Context context) {
        if (instance == null)
            instance = new PersonsSharedPrefs(context);
        return instance;
    }

    public boolean putValue(String[] plainValues) {
        int count = sharedPreferences.getAll().size() + 1;

        for (int i = 0; i < plainValues.length; i++)
            plainValues[i] = utility.encodeIt(plainValues[i]);

        JSONObject encodedValuesJson = new JSONObject();
        try {
            encodedValuesJson.put("name", plainValues[0]);
            encodedValuesJson.put("number", plainValues[1]);
            encodedValuesJson.put("date", plainValues[2]);
            encodedValuesJson.put("time", plainValues[3]);
        } catch (JSONException e) {
            Log.e("ERROR", "JSONException in PersonsSharedPreferences putValue method");
            e.printStackTrace();
        }

        try {
            editor.putString(String.valueOf(count), encodedValuesJson.toString());
            editor.apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean putValue(String key, String[] plainValues) {
        for (int i = 0; i < plainValues.length; i++)
            plainValues[i] = utility.encodeIt(plainValues[i]);

        JSONObject encodedValuesJson = new JSONObject();
        try {
            encodedValuesJson.put("name", plainValues[0]);
            encodedValuesJson.put("number", plainValues[1]);
            encodedValuesJson.put("date", plainValues[2]);
            encodedValuesJson.put("time", plainValues[3]);
        } catch (JSONException e) {
            Log.e("ERROR", "JSONException in PersonsSharedPreferences putValue method");
            e.printStackTrace();
        }

        try {
            editor.putString(key, encodedValuesJson.toString());
            editor.apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public JSONObject getValue(String key) {
        String valueString = sharedPreferences.getString(key, null);

        if (valueString != null) {
            JSONObject valueJson;
            try {
                valueJson = new JSONObject(valueString);
                return valueJson;

            } catch (JSONException e) {
                Log.e("ERROR", "JSONException in PersonsSharedPreferences getValue method");
                e.printStackTrace();
            }
            return null;

        } else
            return null;
    }

    public String getKey(String plainName) {
        for (Map.Entry<String, String[]> entry : keyAndPersons.entrySet())
            if (entry.getValue()[0].equals(plainName))
                return entry.getKey();

        return null;
    }

    public String[] getKeys() {
        Map<String, ?> allPrefs = sharedPreferences.getAll();
        String[] keysArray = new String[allPrefs.size()];
        allPrefs.keySet().toArray(keysArray);

        return keysArray;
    }

    public ArrayList<String[]> getValues() {
        int personsCount = sharedPreferences.getAll().size();
        JSONObject valueJson;
        ArrayList<String[]> valuesArraysList = new ArrayList<>();
        BirthdayUtility utility = new BirthdayUtility();

        for (int i = 1; i <= personsCount; i++) {
            String[] valuesList = new String[4];
            valueJson = this.getValue(String.valueOf(i));

            if (valueJson == null) {
                ++personsCount;
                continue;
            } else {

                try {
                    valuesList[0] = utility.decodeIt(valueJson.getString("name"));
                    valuesList[1] = utility.decodeIt(valueJson.getString("number"));
                    valuesList[2] = utility.decodeIt(valueJson.getString("date"));
                    valuesList[3] = utility.decodeIt(valueJson.getString("time"));

                } catch (JSONException e) {
                    Log.e("ERROR", "JSONException in PersonsSharedPreferences getValues method");
                    e.printStackTrace();
                }
                String[] stringArray = {valuesList[0], valuesList[1]};
                keyAndPersons.put(String.valueOf(i), stringArray);

                valuesArraysList.add(valuesList);
            }
        }
        return valuesArraysList;
    }

    public boolean remove(String plainName) {
        for (Map.Entry<String, String[]> entry : keyAndPersons.entrySet())
            if (entry.getValue()[0].equals(plainName)) {
                editor.remove(entry.getKey());
                editor.apply();
                return true;
            }
        return false;
    }

    public boolean clear() {
        try {
            editor.clear();
            editor.apply();
            return true;
        } catch (Exception e) {
            Log.e("ERROR", "Exception in PersonsSharedPreferences clear method");
            return false;
        }
    }

    public boolean isPersonExited(String plainName) {
        for (Map.Entry<String, String[]> entry : keyAndPersons.entrySet()) {
            if (entry.getValue()[0].equals(plainName))
                return true;
        }
        return false;
    }

    public boolean isPhoneNumberExited(String number) {
        for (Map.Entry<String, String[]> entry : keyAndPersons.entrySet()) {
            if (entry.getValue()[1].equals(number))
                return true;
        }
        return false;
    }
}
