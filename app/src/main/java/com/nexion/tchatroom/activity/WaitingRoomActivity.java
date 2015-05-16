package com.nexion.tchatroom.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nexion.tchatroom.App;
import com.nexion.tchatroom.BeaconOrganizer;
import com.nexion.tchatroom.BluetoothManager;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.api.APIRequester;
import com.nexion.tchatroom.fragment.WaitingRoomFragment;
import com.nexion.tchatroom.manager.KeyFields;
import com.nexion.tchatroom.manager.PlayServicesManager;
import com.nexion.tchatroom.model.User;
import com.squareup.otto.Bus;

import javax.inject.Inject;

public class WaitingRoomActivity extends BaseActivity implements WaitingRoomFragment.OnFragmentInteractionListener, BeaconOrganizer.BeaconOrganizerListener {

    private final static String WAITING_ROOM_FRAGMENT_TAG = "WaitingRoom";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "WaitingRoomActivity";

    @Inject
    Bus bus;

    private BeaconOrganizer beaconOrganizer;
    private Integer mAvailableRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        ((App) getApplication()).inject(this);

        SharedPreferences sharedPref = getSharedPreferences(KeyFields.PREF_FILE, Context.MODE_PRIVATE);
        if (sharedPref.contains(KeyFields.KEY_USER_ID)) {
            User.currentUserId = sharedPref.getInt(KeyFields.KEY_USER_ID, -1);
        } else {
            Log.e(TAG, "No user ID found");
            finish();
        }

        beaconOrganizer = ((App) getApplication()).getBeaconOrganizer();
        APIRequester apiRequester = new APIRequester(getApplicationContext());
        if (checkPlayServices()) {
            new PlayServicesManager(getApplicationContext(), apiRequester);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(WAITING_ROOM_FRAGMENT_TAG);
        if (fragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, WaitingRoomFragment.newInstance(), WAITING_ROOM_FRAGMENT_TAG)
                    .commit();
        }

        if(!checkBluetooth()) {
            requestBluetoothActivation();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
        Integer roomId;
        if ((roomId = BeaconOrganizer.attachListener(this)) != null) {
            onRoomAvailable(roomId);
        }

        if (checkBluetooth()) {
            beaconOrganizer.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        checkBluetooth();
    }

    @Override
    public void onStop() {
        super.onStop();
        bus.unregister(this);
        BeaconOrganizer.detachListener(this);
    }

    @Override
    public void onRoomAvailable(int roomId) {
        mAvailableRoomId = roomId;
        WaitingRoomFragment fragment = getWaitingRoomFragment();
        if (fragment != null) {
            fragment.onRoomAvailable();
        }
    }

    @Override
    public void onRoomUnavailable() {
        mAvailableRoomId = null;
        WaitingRoomFragment fragment = getWaitingRoomFragment();
        if (fragment != null) {
            fragment.onRoomUnavailable();
        }
    }

    @Override
    public void onJoinRoom() {
        if (App.DEBUG) {
            startChatRoomActivity(1);
        } else {
            startChatRoomActivity(mAvailableRoomId);
        }
    }

    @Override
    public void onLogOut() {
        getSharedPreferences(KeyFields.PREF_FILE, Context.MODE_PRIVATE)
                .edit()
                .remove(KeyFields.KEY_TOKEN)
                .apply();
        startLoginActivity();
        finish();
    }

    private WaitingRoomFragment getWaitingRoomFragment() {
        return (WaitingRoomFragment) getSupportFragmentManager().findFragmentByTag(WAITING_ROOM_FRAGMENT_TAG);
    }

    private void startChatRoomActivity(int roomId) {
        Intent intent = ChatRoomActivity.newIntent(this, roomId);
        startActivity(intent);
    }

    private void startLoginActivity() {
        Intent intent = LoginActivity.newIntent(this);
        startActivity(intent);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private boolean checkBluetooth() {
        BluetoothManager bluetoothManager = BluetoothManager.getInstance();
        return bluetoothManager.isBluetoothEnabled();
    }


    private void requestBluetoothActivation() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivity(discoverableIntent);
    }
}
