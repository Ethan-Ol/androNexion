package com.nexion.tchatroom;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by DarzuL on 21/03/2015.
 * <p/>
 * Check bluetooth availability
 */
public abstract class BluetoothManager {

    public static boolean isBluetoothAvailable() {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    public static boolean isBluetoothEnabled() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }
}
