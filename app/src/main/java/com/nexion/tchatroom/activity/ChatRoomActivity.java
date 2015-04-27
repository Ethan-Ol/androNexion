package com.nexion.tchatroom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.api.APIRequester;
import com.nexion.tchatroom.event.OnRoomUnavailableEvent;
import com.nexion.tchatroom.fragment.ChatRoomFragment;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;

import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChatRoomActivity extends FragmentActivity implements ChatRoomFragment.OnFragmentInteractionListener {

    private final static String CHAT_ROOM_TAG = "ChatRoom";
    private APIRequester apiRequester;

    @Inject
    Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ((App) getApplication()).inject(this);

        apiRequester = new APIRequester(getApplicationContext(), bus);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CHAT_ROOM_TAG);
        if (fragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, ChatRoomFragment.newInstance(), CHAT_ROOM_TAG)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
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
        onBackPressed();
    }

    @Override
    public void startKickActivity() {
        startActivity(new Intent(getApplicationContext(), KickActivity.class));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Subscribe
    public void onRoomUnavailable(OnRoomUnavailableEvent event) {
        startActivity(new Intent(getApplicationContext(), WaitingRoomActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        try {
            apiRequester.leaveRoom();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onBackPressed();
    }
}
