package com.nexion.beaconManagment;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.nexion.tchatroom.AndroidBus;
import com.nexion.tchatroom.BluetoothManager;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.event.BluetoothDisabledEvent;
import com.nexion.tchatroom.event.BluetoothEnabledEvent;
import com.nexion.tchatroom.event.OnRoomAvailableEvent;
import com.nexion.tchatroom.event.OnRoomUnavailableEvent;
import com.nexion.tchatroom.model.Beacon;
import com.nexion.tchatroom.model.Room;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

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
    public BeaconOrganizer(Context context) {
        this.m_context = context;
        m_manager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(m_context);
        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        m_manager.getBeaconParsers().add(new BeaconParser(). setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
    }

    @Subscribe
    public void onBluetoothActivated(BluetoothEnabledEvent event){
        start();
    }

    @Subscribe
    public void onBluetoothDesactivated(BluetoothDisabledEvent event){
        stop();
    }

    public void start(){
        m_manager.bind(this);
    }

    public void stop(){
        m_manager.unbind(this);
        currentRoom.setName(null);
    }

    @Override
    public void onBeaconServiceConnect() {
        Region tmpregion;
        m_manager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time ! " + region.getUniqueId());

                if(currentRoom.isExist()) {
                    if (Integer.toString(currentRoom.getId()).compareTo(region.getUniqueId()) == 0) {
                        return;
                    }
                }

                for (Room r : rooms) {
                    if(Integer.toString(r.getId()).compareTo(region.getUniqueId()) == 0) {

                        if(currentRoom.isExist()){
                            bus.post(new OnRoomUnavailableEvent(currentRoom));
                        }
                        currentRoom = r;
                        bus.post(new OnRoomAvailableEvent(currentRoom));
                    }
                }
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "Exit of region : " + region.getUniqueId());
                if(currentRoom.isExist()) {
                    if (Integer.toString(currentRoom.getId()).compareTo(region.getUniqueId()) == 0) {
                        bus.post(new OnRoomUnavailableEvent(currentRoom));
                        currentRoom.setName(null);
                    }
                }
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "Change state of region : " + region.getUniqueId() + " state : " + state);
                if(currentRoom.isExist()) {
                    if (Integer.toString(currentRoom.getId()).compareTo(region.getUniqueId()) == 0) {
                        if(state==0){
                            bus.post(new OnRoomUnavailableEvent(currentRoom));
                            currentRoom.setName(null);
                        }
                        return;
                    }
                }

                if(state != 0) {
                    for (Room r : rooms) {
                        if (Integer.toString(r.getId()).compareTo(region.getUniqueId()) == 0) {

                            if (currentRoom.isExist()) {
                                bus.post(new OnRoomUnavailableEvent(currentRoom));
                            }
                            currentRoom = r;
                            bus.post(new OnRoomAvailableEvent(currentRoom));
                        }
                    }
                }
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
