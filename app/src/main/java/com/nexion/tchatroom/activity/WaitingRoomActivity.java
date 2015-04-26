package com.nexion.tchatroom.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nexion.beaconManagment.BeaconOrganizer;
import com.nexion.tchatroom.App;
import com.nexion.tchatroom.BluetoothManager;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.api.APIRequester;
import com.nexion.tchatroom.event.BluetoothEnabledEvent;
import com.nexion.tchatroom.event.OnRoomAvailableEvent;
import com.nexion.tchatroom.event.OnRoomUnavailableEvent;
import com.nexion.tchatroom.fragment.WaitingRoomFragment;
import com.nexion.tchatroom.manager.CurrentRoomManager;
import com.nexion.tchatroom.manager.PlayServicesManager;
import com.nexion.tchatroom.model.ChatRoom;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;

import java.util.List;

import javax.inject.Inject;

public class WaitingRoomActivity extends FragmentActivity implements WaitingRoomFragment.OnFragmentInteractionListener, APIRequester.RoomJoinListener {

    private final static String WAITING_ROOM_TAG = "WaitingRoom";
    private static final int REQUEST_ENABLE_BT = 150;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "WaitingRoomActivity";

    @Inject
    Bus bus;
    @Inject
    BeaconOrganizer beaconOrganizer;

    CurrentRoomManager currentRoomManager;
    private APIRequester apiRequester;
    private Integer mAvailableRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        ((App) getApplication()).inject(this);

        currentRoomManager = new CurrentRoomManager(getApplicationContext());
        apiRequester = new APIRequester(getApplicationContext(), bus);
        if (checkPlayServices()) {
            new PlayServicesManager(getApplicationContext(), apiRequester);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(WAITING_ROOM_TAG);
        if (fragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, WaitingRoomFragment.newInstance(), WAITING_ROOM_TAG)
                    .commit();
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
        checkPlayServices();
        checkBluetooth();
    }

    @Override
    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Subscribe
    public void onRoomAvailable(OnRoomAvailableEvent event) {
        mAvailableRoomId = event.getRoomId();
    }

    @Subscribe
    public void onRoomUnavailable(OnRoomUnavailableEvent event) {
        mAvailableRoomId = null;
    }

    @Override
    public void onJoinRoom() {
        try {
            apiRequester.joinRoom(mAvailableRoomId, "", this);
            startChatRoom();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRoomJoined(ChatRoom room) {
        startChatRoom();
    }

    private void startChatRoom() {
        startActivity(new Intent(getApplicationContext(), ChatRoomActivity.class));
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


    private void checkBluetooth() {
        BluetoothManager bluetoothManager = BluetoothManager.getInstance();
        if (bluetoothManager.isBluetoothAvailable()) {
            if (!bluetoothManager.isBluetoothEnabled()) {
                requestBluetoothActivation();
            } else {
                beaconOrganizer.start();
            }
        } else {
            Toast.makeText(this, getString(R.string.device_without_bluetooth), Toast.LENGTH_SHORT).show();
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
