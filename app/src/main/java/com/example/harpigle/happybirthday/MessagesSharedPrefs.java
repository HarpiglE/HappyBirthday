package com.example.harpigle.happybirthday;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public final class MessagesSharedPrefs {
    private static final String MESSAGES_SHARED_PREF = "Messages";
    private static MessagesSharedPrefs instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private BirthdayUtility utility = new BirthdayUtility();

    private MessagesSharedPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(
                MESSAGES_SHARED_PREF, Context.MODE_PRIVATE
        );
        editor = sharedPreferences.edit();
    }

    public static MessagesSharedPrefs getInstance(Context context) {
        if (instance == null) {
            instance = new MessagesSharedPrefs(context);
        }
        return instance;
    }

    public boolean put(String plainMessage) {
        editor.putString(String.valueOf(this.getAll().length), utility.encodeIt(plainMessage));
        editor.apply();

        return true;
    }

    public String get(String numericKey) {
        String value = sharedPreferences.getString(numericKey, "KeyNotAvailable");
        if (value.equals("KeyNotAvailable"))
            return null;
        else
            return value;
    }

    public String[] getAll() {
        Map<String, ?> allPrefs = sharedPreferences.getAll();
        String[] values = new String[allPrefs.size()];
        allPrefs.values().toArray(values);

        return values;
    }
}
