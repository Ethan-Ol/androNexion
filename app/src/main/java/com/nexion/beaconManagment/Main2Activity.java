package com.nexion.beaconManagment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.event.OnRoomAvailableEvent;
import com.nexion.tchatroom.event.OnRoomUnavailableEvent;
import com.nexion.tchatroom.model.Beacon;
import com.nexion.tchatroom.model.ChatRoom;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class Main2Activity extends Activity {
    BeaconOrganizer manager;

    @Inject
    Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).inject(this);

        setContentView(R.layout.activity_main2);

//        List<Beacon> beacons = new ArrayList<>();
//        Beacon b = new Beacon();
//        b.setUUID("11111111-1111-1111-1111111111111111");
//        beacons.add(b);
//        ChatRoom a = new ChatRoom();
//        a.setName("Appartement");
//        a.setBeacons(beacons);
//        if (rooms == null) {
//            rooms = new ArrayList<ChatRoom>();
//        }
//        rooms.add(a);

        manager.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Subscribe
    public void OnRoomEntered(OnRoomAvailableEvent event) {
        Log.i("ROOM", "Entered in " + event.getRoomId());
    }

    @Subscribe
    public void OnRoomExited(OnRoomUnavailableEvent event) {
        Log.i("ROOM", "Exited of " + event.getRoomId());
    }
}
