package com.nexion.beaconManagment;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.nexion.tchatroom.R;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

public class ActivityTestBeacon extends Activity implements BeaconConsumer {
    protected static final String TAG = "BeaconListenActivity";
    private BeaconManager beaconManager;

    private static final String UUID = "0000000000000000000000000000000";
    private static final String UUID2 = "11111111-1111-1111-1111111111111111";
    private static final String UUID3 = "11111119-1111-1111-1111111111111111";
    //private static final String UUID2 = "11111111-1111-1111-1111111111111111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_test_beacon);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {

        Region region2 = new Region("myRegion", Identifier.parse(UUID2), null, null);
        Region region3 = new Region("myRegion3", Identifier.parse(UUID3), null, null);
        Region region1 = new Region("myIdentifier1", null, null, null);
        beaconManager.setMonitorNotifier(new MonitorNotifier() {

            @Override
            public void didEnterRegion(Region region) {
                TextView tv = (TextView) findViewById(R.id.dbg_txt_info);
                //tv.setText("Enter in new region : " + region.getUniqueId());

                Log.i(TAG, "I just saw an beacon for the first time ! " + region.getUniqueId());
            }

            @Override
            public void didExitRegion(Region region) {
                TextView tv = (TextView) findViewById(R.id.dbg_txt_info);
                //tv.setText("Exit of region : " + region.getUniqueId());
                Log.i(TAG, "I no longer see an beacon : " + region.getUniqueId());
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                TextView tv = (TextView) findViewById(R.id.dbg_txt_info);
                //tv.setText("New state of region : " + region.getUniqueId() + " state = " + state);
                Log.i(TAG, "I have just switched from seeing/not seeing beacons : " + state + " id = " + region.getUniqueId());
            }
        });

        TextView tv = (TextView) findViewById(R.id.dbg_txt_info);
        tv.setText("Set notifier");
        Log.i(TAG, "Set notifier");

        try {
            beaconManager.startMonitoringBeaconsInRegion(region2);

        } catch (RemoteException e) {
            tv.setText("Set notifier : ERROR");
            Log.i(TAG, "Set notifier : ERROR");
        }
    }

}
