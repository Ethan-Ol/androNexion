package com.nexion.tchatroom.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nexion.tchatroom.R;
import com.nexion.tchatroom.fragment.LoginFragment;


public class MainActivity extends FragmentActivity {

    private static final String PREF_FILE_MAIN = "main";
    private static final String SAVED_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {



            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, LoginFragment.newInstance(getSavedUsername()), LoginFragment.TAG)
                    .commit();
        }
    }

    private String getSavedUsername() {
        SharedPreferences sharedPref = getSharedPreferences(PREF_FILE_MAIN, Context.MODE_PRIVATE);
        return sharedPref.getString(SAVED_USERNAME, "");
    }
}
