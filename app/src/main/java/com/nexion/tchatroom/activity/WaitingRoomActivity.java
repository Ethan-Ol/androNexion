package com.nexion.tchatroom.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nexion.tchatroom.App;
import com.nexion.tchatroom.BeaconOrganizer;
import com.nexion.tchatroom.BluetoothManager;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.api.APIRequester;
import com.nexion.tchatroom.event.BluetoothEnabledEvent;
import com.nexion.tchatroom.fragment.WaitingRoomFragment;
import com.nexion.tchatroom.manager.PlayServicesManager;
import com.squareup.otto.Bus;

import javax.inject.Inject;

public class WaitingRoomActivity extends FragmentActivity implements WaitingRoomFragment.OnFragmentInteractionListener {

    private final static String WAITING_ROOM_TAG = "WaitingRoom";
    private static final int REQUEST_ENABLE_BT = 150;
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

        beaconOrganizer = ((App) getApplication()).getBeaconOrganizer();
        APIRequester apiRequester = new APIRequester(getApplicationContext());
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

    @Override
    public void onRoomAvailable(int roomId) {
        mAvailableRoomId = roomId;
    }

    @Override
    public void onRoomUnavailable() {
        mAvailableRoomId = null;
    }

    @Override
    public void onJoinRoom() {
        startChatRoom(mAvailableRoomId);
    }

    private void startChatRoom(int roomId) {
        Intent intent = ChatRoomActivity.newIntent(this, roomId);
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
