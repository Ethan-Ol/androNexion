package com.nexion.beaconManagment;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.nexion.tchatroom.AndroidBus;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.event.OnRoomAvailableEvent;
import com.nexion.tchatroom.model.Beacon;
import com.nexion.tchatroom.model.Room;
import com.squareup.otto.Bus;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.apache.http.entity.StringEntity;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ethan on 24/03/15.
 */
public class BeaconOrganizer implements BeaconConsumer{

    private static final String TAG = "BeaconOrganizer";

    @Inject
    Bus bus;
    @Inject
    List<Room> rooms;
    @Inject
    Room currentRoom;
    Context m_context;

    private org.altbeacon.beacon.BeaconManager m_manager;

    @Inject
    BeaconOrganizer(Context m_context) {
        this.m_context = m_context;
        m_manager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(m_context);
        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        m_manager.getBeaconParsers().add(new BeaconParser(). setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
    }

    public void start(){
        m_manager.bind(this);
    }

    public void stop(){
        m_manager.unbind(this);
        currentRoom = null;
    }

    @Override
    public void onBeaconServiceConnect() {
        Region tmpregion;
        m_manager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time ! " + region.getUniqueId());

                for (Room r : rooms) {
                    if(Integer.toString(r.getId()).compareTo(region.getUniqueId()) == 0) {
                        bus.post(new OnRoomAvailableEvent(r));
                    }
                }
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon : " + region.getUniqueId());
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons : " +state + " id = " + region.getUniqueId());
            }
        });

        if(rooms!=null)
            Log.i(TAG,"rooms size is " + rooms.size());
        else
            Log.i(TAG,"rooms is null");

        if(rooms != null) {
            for (Room r : rooms) {
                for (Beacon b : r.getBeacons()) {
                    tmpregion = new Region(""+r.getId(), Identifier.parse(b.getUUID()), null, null);
                    try {
                        m_manager.startMonitoringBeaconsInRegion(tmpregion);
                    } catch (RemoteException e) {
                        Log.i(TAG, "Set notifier : ERROR");
                    }
                }
            }
        }


    }

    @Override
    public Context getApplicationContext() {
        return m_context;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        m_context.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return m_context.bindService(intent,serviceConnection,i);
    }
}
