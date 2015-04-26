package com.nexion.tchatroom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.api.APIRequester;
import com.nexion.tchatroom.event.LoadingEvent;
import com.nexion.tchatroom.fragment.LoginFragment;
import com.nexion.tchatroom.manager.KeyFields;
import com.nexion.tchatroom.manager.TokenManager;
import com.nexion.tchatroom.model.User;
import com.squareup.otto.Bus;

import org.json.JSONException;

import javax.inject.Inject;

public class LoginActivity extends FragmentActivity implements LoginFragment.OnFragmentInteractionListener, APIRequester.UserInfoListener, KeyFields {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private final static String LOGIN_FRAGMENT_TAG = "LoginFragment";

    @Inject
    Bus bus;

    private APIRequester apiRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ((App) getApplication()).inject(this);

        apiRequester = new APIRequester(getApplicationContext(), bus);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(LOGIN_FRAGMENT_TAG);
        if (fragment == null) {
            TokenManager tokenManager = new TokenManager(getApplicationContext());

            if (tokenManager.isExist()) {
                startWaitingRoom();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, LoginFragment.newInstance(), LOGIN_FRAGMENT_TAG)
                        .commit();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override

    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    public void onLogin(String username, String password) {
        try {
            apiRequester.requestToken(username, password, LoginActivity.this);
            bus.post(new LoadingEvent());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startWaitingRoom() {
        startActivity(new Intent(getApplicationContext(), WaitingRoomActivity.class));
        finish();
    }

    @Override
    public void onUserConnected(String token, User user) {
        getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_TOKEN, token)
                .putInt(KEY_USER_ID, user.getId())
                .putString(KEY_USER_PSEUDO, user.getPseudo())
                .putBoolean(KEY_USER_ACL, user.isAdmin())
                .apply();

        startWaitingRoom();
    }
}
