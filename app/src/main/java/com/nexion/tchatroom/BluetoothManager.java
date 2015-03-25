package com.nexion.tchatroom;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by DarzuL on 21/03/2015.
 */
public class BluetoothManager {
    private static BluetoothManager ourInstance = new BluetoothManager();

    public static BluetoothManager getInstance() {
        return ourInstance;
    }

    BluetoothAdapter mBluetoothAdapter;
    BluetoothReceiver bluetoothReceiver;

    private BluetoothManager() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isBluetoothAvailable() {
        return mBluetoothAdapter != null;
    }

    public boolean isBluetoothEnabled() {
        return mBluetoothAdapter.isEnabled();
    }

    public void startDiscovering() {
        if(mBluetoothAdapter != null) {
            mBluetoothAdapter.startDiscovery();
        }
    }

    public BluetoothReceiver getBluetoothReceiver() {
        return bluetoothReceiver;
    }
}
