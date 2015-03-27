package com.nexion.tchatroom.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.nexion.tchatroom.R;
import com.nexion.tchatroom.event.OnRoomAvailableEvent;
import com.nexion.tchatroom.fragment.WaitingRoomFragment;
import com.nexion.tchatroom.manager.CurrentRoomManager;
import com.nexion.tchatroom.model.Room;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class WaitingRoomActivity extends FragmentActivity implements WaitingRoomFragment.OnFragmentInteractionListener {

    private final static String WAITING_ROOM_TAG = "WaitingRoom";

    @Inject
    Bus bus;

    private Room mAvailableRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);


        Fragment fragment = getSupportFragmentManager().findFragmentByTag(WAITING_ROOM_TAG);
        if(fragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, WaitingRoomFragment.newInstance(), WAITING_ROOM_TAG)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }
    @Override

    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    public void onJoinRoom() {
        new CurrentRoomManager(getApplicationContext()).set(mAvailableRoom);
        startActivity(new Intent(getApplicationContext(), ChatRoomActivity.class));
        finish();
    }

    @Subscribe
    public void onRoomAvailable(OnRoomAvailableEvent event) {
        mAvailableRoom = event.getRoom();
    }
}
