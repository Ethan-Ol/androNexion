package com.nexion.tchatroom;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.nexion.tchatroom.event.MessageReceivedEvent;
import com.squareup.otto.Bus;

import javax.inject.Inject;

public class PushService extends IntentService {
    private static final String ACTION_POST = "com.nexion.tchatroom.action.POST";
    private static final String ACTION_JOIN = "com.nexion.tchatroom.action.JOIN";
    private static final String ACTION_LEAVE = "com.nexion.tchatroom.action.LEAVE";

    private static final String EXTRA_JSON = "com.nexion.tchatroom.extra.JSON";

    public static void startActionPost(Context context, String jsonObject) {
        Intent intent = new Intent(context, PushService.class);
        intent.setAction(ACTION_POST);
        intent.putExtra(EXTRA_JSON, jsonObject);
        context.startService(intent);
    }

    public static void startActionJoin(Context context, String jsonObject) {
        Intent intent = new Intent(context, PushService.class);
        intent.setAction(ACTION_JOIN);
        intent.putExtra(EXTRA_JSON, jsonObject);
        context.startService(intent);
    }

    public static void startActionLeave(Context context, String jsonObjectStr) {
        Intent intent = new Intent(context, PushService.class);
        intent.setAction(ACTION_LEAVE);
        intent.putExtra(EXTRA_JSON, jsonObjectStr);
        context.startService(intent);
    }

    @Inject
    Bus bus;

    public PushService() {
        super("PushService");
        ((App) getApplication()).inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final String jsonObjectStr = intent.getStringExtra(EXTRA_JSON);
            if (ACTION_POST.equals(action)) {
                handleActionPost(jsonObjectStr);
            } else if (ACTION_JOIN.equals(action)) {
                handleActionJoin(jsonObjectStr);
            } else if (ACTION_LEAVE.equals(action)) {
                handleActionLeave(jsonObjectStr);
            }
        }
    }

    private void handleActionPost(String jsonObjectStr) {
        //bus.post(new MessageReceivedEvent());
    }

    private void handleActionJoin(String jsonObjectStr) {
        //bus.post();
    }

    private void handleActionLeave(String jsonObjectStr) {
        //bus.post();
    }
}
