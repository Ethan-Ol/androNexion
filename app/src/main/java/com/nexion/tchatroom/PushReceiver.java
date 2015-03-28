package com.nexion.tchatroom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class PushReceiver extends BroadcastReceiver {

    private static final String TAG = "PushReceiver";

    public PushReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object message = bundle.get("message");
        Log.d(TAG, "message = " + message);

        /*
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            Log.d(TAG, String.format("%s %s (%s)", key,
                    value.toString(), value.getClass().getName()));
        }
        //*/
    }
}
