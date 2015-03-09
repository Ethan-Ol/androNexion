package com.nexion.tchatroom.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.fragment.ChatRoomFragment;
import com.nexion.tchatroom.fragment.LoginFragment;
import com.nexion.tchatroom.model.Token;

import java.util.Calendar;

import javax.inject.Inject;

public class MainActivity extends FragmentActivity implements
        LoginFragment.OnFragmentInteractionListener,
        ChatRoomFragment.OnFragmentInteractionListener {

    @Inject
    Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((App) getApplication()).inject(this);

        if (savedInstanceState == null) {

            if(token.isEmpty()) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, LoginFragment.newInstance(token.getUsername()), LoginFragment.TAG)
                        .commit();
            }
            else if(token.isValid()) {
                // TODO Launch main fragment
            }
            else {
                // TODO refresh token
            }
        }
    }

    @Override
    public void onLogin(String username, String password) {
        //TODO contact API
    }

    @Override
    public void sendMessage(Uri uri) {
        //TODO API
    }
}
