package com.nexion.tchatroom.model;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by DarzuL on 09/03/2015.
 */
public class Token {
    private static final String PREF_FILE_MAIN = "main";
    private static final String SAVED_TOKEN = "key";

    String key;

    @Inject
    public Token(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_MAIN, Context.MODE_PRIVATE);
        key = sharedPref.getString(SAVED_TOKEN, "");
    }

    public boolean isEmpty() {
        return key.isEmpty();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
