package com.nexion.tchatroom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

public class PushReceiver extends BroadcastReceiver {

    private static final String TAG = "PushReceiver";
    private static final String ACTION = "action";
    private static final String ACTION_LEAVE = "leave";
    private static final String ACTION_JOIN = "join";
    private static final String ACTION_KICK = "kick";
    private static final String ACTION_POST = "post";

    public PushReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object message = bundle.get("message");
        Log.d(TAG, "message = " + message);

        try {
            JSONObject object = new JSONObject(message.toString());
            String action = object.getString(ACTION);

            if (action.equals(ACTION_POST)) {
                PushService.startActionPost(context, message.toString());
            } else if (action.equals(ACTION_LEAVE)) {
                PushService.startActionLeave(context, message.toString());
            } else if (action.equals(ACTION_JOIN)) {
                PushService.startActionJoin(context, message.toString());
            } else if (action.equals(ACTION_KICK)) {
                PushService.startActionKick(context, message.toString());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
