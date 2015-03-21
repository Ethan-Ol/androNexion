package com.nexion.tchatroom.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
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
import com.nexion.tchatroom.fragment.ChatRoomFragment;
import com.nexion.tchatroom.fragment.LoginFragment;
import com.nexion.tchatroom.fragment.NoChatRoomFragment;
import com.nexion.tchatroom.model.Token;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;

import javax.inject.Inject;

public class MainActivity extends FragmentActivity implements
        LoginFragment.OnFragmentInteractionListener,
        ChatRoomFragment.OnFragmentInteractionListener {

    private static final int REQUEST_ENABLE_BT = 150;

    @Inject
    Token token;

    @Inject
    APIRequester apiRequester;

    @Inject
    Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((App) getApplication()).inject(this);
        checkBluetooth();

        if (savedInstanceState == null) {
            if (token.isEmpty()) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, LoginFragment.newInstance(null), LoginFragment.TAG)
                        .commit();
            } else {
                onTokenReceived(null);
                bus.post(new LoadingEvent());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
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
    public void displayMainFragment(RoomsInfoReceivedEvent event) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new NoChatRoomFragment(), NoChatRoomFragment.TAG)
                .commit();
    }

    @Override
    public void sendMessage(String content) {
        try {
            apiRequester.postMessage(content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkBluetooth() {
        BluetoothManager bluetoothManager = BluetoothManager.getInstance();
        if (bluetoothManager.isBluetoothAvailable()) {
            if(!bluetoothManager.isBluetoothEnabled()) {
                requestBluetoothActivation();
            }
        }
        else {
            Toast.makeText(this, getString(R.string.device_without_bluetooth), Toast.LENGTH_SHORT);
        }
    }

    private void requestBluetoothActivation() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(discoverableIntent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    bus.post(new BluetoothEnabledEvent());
                }
                break;
        }
    }
}
