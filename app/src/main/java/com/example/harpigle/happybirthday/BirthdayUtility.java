package com.example.harpigle.happybirthday;

import com.example.harpigle.happybirthday.Messages.MessagesSharedPrefs;

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

    public boolean isMessageExited(MessagesSharedPrefs messagesSharedPrefs, String plainMessage) {
        String encodedMessage = this.encodeIt(plainMessage);
        ArrayList<String> values = messagesSharedPrefs.getValues();

        for (String value : values)
            if (value.equals(encodedMessage))
                return true;

        return false;
    }
}
