package com.nexion.tchatroom.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nexion.tchatroom.R;
import com.nexion.tchatroom.fragment.WaitingRoomFragment;

public class ChatRoomActivity extends ActionBarActivity {

    private final static String CHAT_ROOM_TAG = "ChatRoom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CHAT_ROOM_TAG);
        if(fragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, WaitingRoomFragment.newInstance(), CHAT_ROOM_TAG)
                    .commit();
        }
    }


}
