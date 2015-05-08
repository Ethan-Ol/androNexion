package com.nexion.tchatroom;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by DarzuL on 21/03/2015.
 * <p/>
 * Check bluetooth availability
 */
public class BluetoothManager {
    private static BluetoothManager ourInstance = new BluetoothManager();

    public static BluetoothManager getInstance() {
        return ourInstance;
    }

    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothManager() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isBluetoothAvailable() {
        return mBluetoothAdapter != null;
    }

    public boolean isBluetoothEnabled() {
        return mBluetoothAdapter.isEnabled();
    }
}
