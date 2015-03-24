package com.nexion.beaconManagment;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.model.Beacon;
import com.nexion.tchatroom.model.Room;

import org.altbeacon.beacon.BeaconManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class Main2Activity extends Activity {

    @Inject
    List<Room> rooms;

    @Inject
    BeaconOrganizer manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).inject(this);

        setContentView(R.layout.activity_main2);

        List<Beacon> beacons = new ArrayList<Beacon>();
        Beacon b = new Beacon();
        b.setUUID("11111111-1111-1111-1111111111111111");
        beacons.add(b);
        Room a = new Room();
        a.setName("Appartement");
        a.setBeacons(beacons);
        if(rooms == null){
            rooms = new ArrayList<Room>();
        }
        rooms.add(a);

        manager.start();
    }
}
