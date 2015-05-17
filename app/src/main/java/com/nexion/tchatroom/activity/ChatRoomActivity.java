package com.nexion.tchatroom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nexion.tchatroom.BeaconOrganizer;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.api.APIRequester;
import com.nexion.tchatroom.api.ErrorHandler;
import com.nexion.tchatroom.fragment.ChatRoomFragment;
import com.nexion.tchatroom.fragment.KickFragment;
import com.nexion.tchatroom.manager.KeyFields;
import com.nexion.tchatroom.model.ChatRoom;
import com.nexion.tchatroom.model.User;

import org.json.JSONException;

import java.util.List;

public class ChatRoomActivity extends BaseActivity implements ChatRoomFragment.OnFragmentInteractionListener,
        BeaconOrganizer.BeaconOrganizerListener, APIRequester.RoomJoinListener, KickFragment.OnFragmentInteractionListener,
        APIRequester.KickListener {

    private final static String CHAT_ROOM_FRAGMENT_TAG = "ChatRoom";
    private final static String KICK_FRAGMENT_TAG = "Kick";
    private final static String EXTRA_ROOM_ID = "room_id";

    public static Intent newIntent(Context context, int roomId) {
        Intent intent = new Intent(context.getApplicationContext(), ChatRoomActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomId);

        return intent;
    }

    private ProgressBar mLoaderPb;
    private APIRequester apiRequester;
    private ChatRoom mChatRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

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
    public void sendMessage(String content) {
        try {
            apiRequester.postMessage(content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRoomLeaved() {
        onBackPressed();
    }

    @Override
    public void startKickFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(KICK_FRAGMENT_TAG);
        if (fragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, KickFragment.newInstance(), KICK_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onCurrentUserKicked() {
        finish();
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

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CHAT_ROOM_FRAGMENT_TAG);
        if (fragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, ChatRoomFragment.newInstance(), CHAT_ROOM_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public ChatRoom fragmentCreated() {
        return mChatRoom;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Integer statusCode = ErrorHandler.getStatusCode(error);
        if(statusCode == null)
            return;

        switch (statusCode) {
            case 403:
                Toast.makeText(getApplicationContext(), R.string.text_impossible_to_join, Toast.LENGTH_LONG).show();
                finish();
                break;

            case 498:
                Toast.makeText(getApplicationContext(), R.string.text_token_changed, Toast.LENGTH_LONG).show();
                getSharedPreferences(KeyFields.PREF_FILE, Context.MODE_PRIVATE)
                        .edit()
                        .remove(KeyFields.KEY_TOKEN)
                        .apply();
                startActivity(LoginActivity.newIntent(this));
                setResult(WaitingRoomActivity.RESULT_LOG_OUT);
                finish();
                break;

            default:
                ErrorHandler.toastError(this, error);
        }
    }

    @Override
    public void onKick(List<User> userSelected) {
        for (User user : userSelected) {
            try {
                apiRequester.kickUser(user, this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(KICK_FRAGMENT_TAG);
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
