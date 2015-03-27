package com.nexion.tchatroom.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DarzuL on 27/03/2015.
 */
public class TokenManager implements KeyFields, IManager<String> {

    private String token = "";
    private final SharedPreferences sharedPref;

    public TokenManager(Context context) {
        sharedPref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isExist() {
        if(token.isEmpty()) {
            token = sharedPref.getString(KEY_TOKEN, "");
        }

        return !token.isEmpty();
    }

    @Override
    public void set(String token) {
        this.token = token;

        sharedPref.edit()
                .putString(KEY_TOKEN, token)
                .apply();
    }

    @Override
    public String get() {
        if(token.isEmpty()) {
            token = sharedPref.getString(KEY_TOKEN, "");
        }

        return token;
    }
}
