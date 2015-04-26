package com.nexion.beaconManagment;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;

import com.nexion.tchatroom.event.BluetoothDisabledEvent;
import com.nexion.tchatroom.event.BluetoothEnabledEvent;
import com.nexion.tchatroom.event.OnRoomAvailableEvent;
import com.nexion.tchatroom.event.OnRoomUnavailableEvent;
import com.nexion.tchatroom.event.RoomsInfoReceivedEvent;
import com.nexion.tchatroom.model.Beacon;
import com.nexion.tchatroom.model.BeaconRoom;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by ethan on 24/03/15.
 */
@Singleton
public class BeaconOrganizer implements BeaconConsumer {

    private static final String TAG = "BeaconOrganizer";

    @Inject
    Bus bus;

    List<BeaconRoom> rooms;
    BeaconRoom currentRoom;
    Context m_context;

    private org.altbeacon.beacon.BeaconManager m_manager;
    boolean started;

    @Inject
    public BeaconOrganizer(Context context) {
        this.m_context = context;
        this.rooms = rooms; // TODO APIRequester
        started = false;

        m_manager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(m_context);
        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        m_manager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
    }

    @Subscribe
    public void onBluetoothActivated(BluetoothEnabledEvent event) {
        start();
    }

    @Subscribe
    public void onBluetoothDesactivated(BluetoothDisabledEvent event) {
        stop();
    }

    @Subscribe
    public void onRoomsIsReceived(RoomsInfoReceivedEvent event) {
        Log.i(TAG, "Room received");
        stop();
        start();
    }

    public void start() {
        if (started == false) {
            Log.i(TAG,"Start " + TAG);
            bus.register(this);
            started = true;
            m_manager.bind(this);
        }
    }

    public void stop() {
        if (started == true) {
            started = false;
            m_manager.unbind(this);
            bus.unregister(this);
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        Region tmpregion;
        m_manager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time ! " + region.getUniqueId());

                if (currentRoom != null) {
                    if (Integer.toString(currentRoom.getId()).compareTo(region.getUniqueId()) == 0) {
                        return;
                    }
                }

                for (BeaconRoom r : rooms) {
                    if (Integer.toString(r.getId()).compareTo(region.getUniqueId()) == 0) {

                        if (currentRoom != null) {
                            bus.post(new OnRoomUnavailableEvent(currentRoom.getId()));
                        }
                        currentRoom = r;
                        bus.post(new OnRoomAvailableEvent(currentRoom.getId()));
                    }
                }
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "Exit of region : " + region.getUniqueId());
                if (currentRoom != null) {
                    if (Integer.toString(currentRoom.getId()).compareTo(region.getUniqueId()) == 0) {
                        bus.post(new OnRoomUnavailableEvent(currentRoom.getId()));
                        currentRoom = null;
                    }
                }
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "Change state of region : " + region.getUniqueId() + " state : " + state);
                if (currentRoom != null) {
                    if (Integer.toString(currentRoom.getId()).compareTo(region.getUniqueId()) == 0) {
                        if (state == 0) {
                            bus.post(new OnRoomUnavailableEvent(currentRoom.getId()));
                            currentRoom = null;
                        }
                        return;
                    }
                }

                if (state != 0) {
                    for (BeaconRoom r : rooms) {
                        if (Integer.toString(r.getId()).compareTo(region.getUniqueId()) == 0) {

                            if (currentRoom != null) {
                                bus.post(new OnRoomUnavailableEvent(currentRoom.getId()));
                            }
                            currentRoom = r;
                            bus.post(new OnRoomAvailableEvent(currentRoom.getId()));
                        }
                    }
                }
            }
        });

        if (rooms != null)
            Log.i(TAG, "rooms size is " + rooms.size());
        else
            Log.i(TAG, "rooms is null");

        int i=0;

        if (rooms != null) {
            for (BeaconRoom r : rooms) {
                for (Beacon b : r.getBeacons()) {
                    try {
                        tmpregion = new Region("" + r.getId(), Identifier.parse(b.getUUID()), null, null);
                        m_manager.startMonitoringBeaconsInRegion(tmpregion);

                        Log.i(TAG, "Listen Beacon : " + b.getUUID() + " room " + r.getId());

                    } catch (RemoteException e) {
                        Log.i(TAG, "Set notifier : ERROR");
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
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
        return m_context.bindService(intent, serviceConnection, i);
    }
}
