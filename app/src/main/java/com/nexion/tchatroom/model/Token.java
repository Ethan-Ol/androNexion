package com.nexion.tchatroom.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

import javax.inject.Inject;

/**
 * Created by DarzuL on 09/03/2015.
 */
public class Token {
    private static final String PREF_FILE_MAIN = "main";
    private static final String SAVED_TOKEN = "token";
    private static final String SAVED_TOKEN_VALIDITY = "token_validity";
    private static final String SAVED_USERNAME = "username";

    String token;
    long validity;
    String username;

    @Inject
    public Token(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_MAIN, Context.MODE_PRIVATE);
        token = sharedPref.getString(SAVED_TOKEN, "");
        validity = sharedPref.getLong(SAVED_TOKEN_VALIDITY, 0);
        username = sharedPref.getString(SAVED_USERNAME, "");
    }

    public boolean isEmpty() {
        return token.isEmpty();
    }

    public boolean isValid() {
        return validity > Calendar.getInstance().getTimeInMillis();
    }

    public String getUsername() {
        return username;
    }
}
