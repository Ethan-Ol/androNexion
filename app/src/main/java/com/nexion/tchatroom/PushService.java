package com.nexion.tchatroom;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.nexion.tchatroom.event.JoinReceivedEvent;
import com.nexion.tchatroom.event.LeaveReceivedEvent;
import com.nexion.tchatroom.event.MessageReceivedEvent;
import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.User;
import com.squareup.otto.Bus;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class PushService extends IntentService {
    private static final String ACTION_POST = "com.nexion.tchatroom.action.POST";
    private static final String ACTION_JOIN = "com.nexion.tchatroom.action.JOIN";
    private static final String ACTION_LEAVE = "com.nexion.tchatroom.action.LEAVE";

    private static final String EXTRA_JSON = "com.nexion.tchatroom.extra.JSON";

    private static final String NAME = "pseudo";
    private static final String ID = "id";
    private static final String DATE = "dateTime";
    private static final String MESSAGE = "message";

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
        try {
            JSONObject jobj = new JSONObject(jsonObjectStr);
            NexionMessage message = new NexionMessage();
            message.setContent(jobj.getString(MESSAGE));
            User author = new User();
            author.setPseudo(jobj.getString(NAME));
            author.setId(jobj.getInt(ID));
            message.setAuthor(author);
            bus.post(new MessageReceivedEvent(message));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //bus.post(new MessageReceivedEvent());
    }

    private void handleActionJoin(String jsonObjectStr) {
        try {
            JSONObject jobj = new JSONObject(jsonObjectStr);
            User author = new User();
            author.setPseudo(jobj.getString(NAME));
            author.setId(jobj.getInt(ID));
            bus.post(new JoinReceivedEvent(author));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //bus.post();
    }

    private void handleActionLeave(String jsonObjectStr) {
        try {
            JSONObject jobj = new JSONObject(jsonObjectStr);
            User author = new User();
            author.setPseudo(jobj.getString(NAME));
            author.setId(jobj.getInt(ID));
            bus.post(new LeaveReceivedEvent(author));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //bus.post();
    }
}
