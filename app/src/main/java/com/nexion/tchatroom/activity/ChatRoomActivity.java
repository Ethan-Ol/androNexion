package com.nexion.tchatroom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.api.APIRequester;
import com.nexion.tchatroom.fragment.ChatRoomFragment;
import com.nexion.tchatroom.model.Room;
import com.squareup.otto.Bus;

import org.json.JSONException;

import java.util.List;

import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChatRoomActivity extends FragmentActivity implements ChatRoomFragment.OnFragmentInteractionListener {

    private final static String CHAT_ROOM_TAG = "ChatRoom";
    private APIRequester apiRequester;

    @Inject
    Bus bus;
    @Inject
    List<Room> rooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ((App) getApplication()).inject(this);

        apiRequester = new APIRequester(getApplicationContext(), bus, rooms);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CHAT_ROOM_TAG);
        if (fragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, ChatRoomFragment.newInstance(), CHAT_ROOM_TAG)
                    .commit();
        }
    }


    @Override
    public void sendMessage(String content) {
        try {
            apiRequester.postMessage(content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void leaveRoom() {
        try {
            apiRequester.leaveRoom();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(getApplicationContext(), WaitingRoomActivity.class));
    }

    @Override
    public void startKickActivity() {
        startActivity(new Intent(getApplicationContext(), KickActivity.class));
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
