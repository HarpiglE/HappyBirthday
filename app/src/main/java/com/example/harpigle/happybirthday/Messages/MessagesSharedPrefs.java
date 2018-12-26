package com.example.harpigle.happybirthday.Messages;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.harpigle.happybirthday.BirthdayUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class MessagesSharedPrefs {

    private static final String MESSAGES_SHARED_PREF = "Messages";
    private static MessagesSharedPrefs instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private BirthdayUtility utility = new BirthdayUtility();
    private HashMap<String, String> keyAndMessages = new HashMap<>();

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

    public boolean putValue(String plainMessage) {
        int count = this.getAll().size() + 1;
        editor.putString(String.valueOf(count), utility.encodeIt(plainMessage));
        editor.apply();
        return true;
    }

    public boolean putValue(String key, String plainMessage) {
        editor.putString(key, utility.encodeIt(plainMessage));
        editor.apply();
        return true;
    }

    public String getValue(String key) {
        String value = sharedPreferences.getString(key, "KeyNotAvailable");
        if (value.equals("KeyNotAvailable"))
            return null;
        else
            return value;
    }

    public String getKey(String plainMessage) {
        for (Map.Entry<String, String> entry : keyAndMessages.entrySet())
            if (entry.getValue().equals(plainMessage))
                return entry.getKey();

        return null;
    }

    public ArrayList<String> getAll() {
        int messagesCount = this.getKeys().length;
        ArrayList<String> sortedValues = new ArrayList<>();
        String retrievedMessage;

        for (int i = 0; i < messagesCount; i++) {
            retrievedMessage = this.getValue(String.valueOf(i + 1));
            if (retrievedMessage == null) {
                ++messagesCount;
                continue;
            } else {
                keyAndMessages.put(String.valueOf(i + 1), utility.decodeIt(retrievedMessage));
                sortedValues.add(utility.decodeIt(retrievedMessage));
            }
        }
        return sortedValues;
    }

    public String[] getKeys() {
        Map<String, ?> allPrefs = sharedPreferences.getAll();

        String[] keysArray = new String[allPrefs.size()];
        allPrefs.keySet().toArray(keysArray);

        return keysArray;
    }

    public boolean remove(String value) {
        for (Map.Entry<String, String> k : keyAndMessages.entrySet())
            if (k.getValue().equals(value)) {
                editor.remove(k.getKey());
                editor.apply();
                return true;
            }

        return false;
    }

    public boolean clear() {
        editor.clear();
        editor.apply();
        return true;
    }
}
