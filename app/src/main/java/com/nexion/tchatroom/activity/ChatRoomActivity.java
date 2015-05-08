package com.nexion.tchatroom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.nexion.tchatroom.App;
import com.nexion.tchatroom.BeaconOrganizer;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.api.APIRequester;
import com.nexion.tchatroom.api.ErrorHandler;
import com.nexion.tchatroom.fragment.ChatRoomFragment;
import com.nexion.tchatroom.model.ChatRoom;
import com.squareup.otto.Bus;

import org.json.JSONException;

import javax.inject.Inject;

public class ChatRoomActivity extends BaseActivity implements ChatRoomFragment.OnFragmentInteractionListener, BeaconOrganizer.BeaconOrganizerListener, APIRequester.RoomJoinListener {

    private final static String CHAT_ROOM_TAG = "ChatRoom";
    private final static String EXTRA_ROOM_ID = "room_id";

    public static Intent newIntent(Context context, int roomId) {
        Intent intent = new Intent(context.getApplicationContext(), ChatRoomActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomId);

        return intent;
    }

    private ProgressBar mLoaderPb;
    private APIRequester apiRequester;
    private ChatRoom mChatRoom;

    @Inject
    Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ((App) getApplication()).inject(this);

        mLoaderPb = (ProgressBar) findViewById(R.id.progressBar);

        apiRequester = new APIRequester(getApplicationContext());
        if (!getIntent().hasExtra(EXTRA_ROOM_ID)) {
            finish();
        }

        int roomId = getIntent().getIntExtra(EXTRA_ROOM_ID, 0);
        try {
            mLoaderPb.setVisibility(View.VISIBLE);
            apiRequester.joinRoom(roomId, "", this);
        } catch (JSONException e) {
            e.printStackTrace();
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
    public void onRoomAvailable(int roomId) {
        // TODO Another room is available
    }

    @Override
    public void onRoomUnavailable() {
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

    @Override
    public void onRoomJoined(ChatRoom room) {
        mChatRoom = room;
        mLoaderPb.setVisibility(View.GONE);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CHAT_ROOM_TAG);
        if (fragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, ChatRoomFragment.newInstance(), CHAT_ROOM_TAG)
                    .commit();
        }
    }

    @Override
    public ChatRoom fragmentCreated() {
        return mChatRoom;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ErrorHandler.toastError(this, error);
    }
}
