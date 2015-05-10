package com.nexion.tchatroom.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class WaitingRoomActivity extends BaseActivity implements WaitingRoomFragment.OnFragmentInteractionListener, BeaconOrganizer.BeaconOrganizerListener {

    private final static String WAITING_ROOM_FRAGMENT_TAG = "WaitingRoom";
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

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(WAITING_ROOM_FRAGMENT_TAG);
        if (fragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, WaitingRoomFragment.newInstance(), WAITING_ROOM_FRAGMENT_TAG)
                    .commit();
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
        if(fragment != null) {
            fragment.onRoomAvailable();
        }
    }

    @Override
    public void onRoomUnavailable() {
        mAvailableRoomId = null;
        WaitingRoomFragment fragment = getWaitingRoomFragment();
        if(fragment != null) {
            fragment.onRoomUnavailable();
        }
    }

    @Override
    public void onJoinRoom() {
        if(App.DEBUG) {
            startChatRoom(1);
        }
        else {
            startChatRoom(mAvailableRoomId);
        }
    }

    private WaitingRoomFragment getWaitingRoomFragment() {
        return (WaitingRoomFragment) getSupportFragmentManager().findFragmentByTag(WAITING_ROOM_FRAGMENT_TAG);
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
