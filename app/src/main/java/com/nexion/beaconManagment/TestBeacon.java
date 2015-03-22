package com.nexion.beaconManagment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import com.nexion.tchatroom.model.Beacon;

import org.altbeacon.beacon.BeaconConsumer;

/**
 * Created by ethan on 20/03/15.
 */
public class TestBeacon extends Activity implements BeaconConsumer{

    @Override
    public void onBeaconServiceConnect() {

    }

}
