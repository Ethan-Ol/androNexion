package com.nexion.tchatroom.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.nexion.tchatroom.model.User;

/**
 * Created by DarzuL on 27/03/2015.
 *
 * Current user manager
 */
public class CurrentUserManager implements KeyFields, IManager<User> {
    private User user;
    private final SharedPreferences sharedPref;

    public CurrentUserManager(Context context) {
        sharedPref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isExist() {
        return user != null;
    }

    @Override
    public void set(User user) {
        sharedPref.edit()
                .putString(KEY_PSEUDO, user.getPseudo())
                .putBoolean(KEY_ADMIN, user.isAdmin())
                .apply();
    }

    @Override
    public User get() {
        if(user == null) {
            String pseudo = sharedPref.getString(KEY_PSEUDO, "");
            boolean isAdmin = sharedPref.getBoolean(KEY_PSEUDO, false);
            user = new User(pseudo, isAdmin);
        }

        return user;
    }
}
