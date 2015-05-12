package com.nexion.tchatroom;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class BluetoothReceiver extends BroadcastReceiver {
    private static final String TAG = "BluetoothReceiver";

    public BluetoothReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        // When bluetooth state changed
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {

            Bundle extras = intent.getExtras();
            int currentState = extras.getInt(BluetoothAdapter.EXTRA_STATE);
            if (currentState == BluetoothAdapter.STATE_ON) {
                Log.i(TAG, "Bluetooth ON");
                ScanService.startActionScan(context);
            } else if (currentState == BluetoothAdapter.STATE_OFF) {
                Log.i(TAG, "Bluetooth OFF");
                ScanService.stopActionScan(context);
            }
        }
    }
}
