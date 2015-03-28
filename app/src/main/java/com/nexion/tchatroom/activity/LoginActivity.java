package com.nexion.tchatroom.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.BluetoothManager;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.api.APIRequester;
import com.nexion.tchatroom.event.BluetoothEnabledEvent;
import com.nexion.tchatroom.event.LoadingEvent;
import com.nexion.tchatroom.event.RoomsInfoReceivedEvent;
import com.nexion.tchatroom.event.TokenReceivedEvent;
import com.nexion.tchatroom.event.UserInfoReceivedEvent;
import com.nexion.tchatroom.fragment.LoginFragment;
import com.nexion.tchatroom.manager.CurrentUserManager;
import com.nexion.tchatroom.manager.TokenManager;
import com.nexion.tchatroom.model.Room;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;

import java.util.List;

import javax.inject.Inject;

public class LoginActivity extends FragmentActivity implements LoginFragment.OnFragmentInteractionListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private final static String LOGIN_FRAGMENT_TAG = "LoginFragment";

    @Inject
    Bus bus;
    @Inject
    List<Room> rooms;

    private APIRequester apiRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ((App) getApplication()).inject(this);

        //TODO delete
        //new TokenManager(getApplicationContext()).set("");

        apiRequester = new APIRequester(getApplicationContext(), bus, rooms);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(LOGIN_FRAGMENT_TAG);
        if (fragment == null) {
            TokenManager tokenManager = new TokenManager(getApplicationContext());

            if (tokenManager.isExist()) {
                startWaitingRoom(null);
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
            apiRequester.requestToken(username, password);
            bus.post(new LoadingEvent());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onTokenReceived(TokenReceivedEvent event) {
        try {
            apiRequester.requestUserInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onUserInfoReceived(UserInfoReceivedEvent event) {
        try {
            apiRequester.requestRoomsInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void startWaitingRoom(RoomsInfoReceivedEvent event) {
        startActivity(new Intent(getApplicationContext(), WaitingRoomActivity.class));
        finish();
    }
}
