package com.nexion.tchatroom;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;

import com.nexion.tchatroom.api.APIRequester;
import com.nexion.tchatroom.event.BluetoothDisabledEvent;
import com.nexion.tchatroom.event.BluetoothEnabledEvent;
import com.nexion.tchatroom.model.Beacon;
import com.nexion.tchatroom.model.BeaconRoom;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ethan on 24/03/15.
 */
public class BeaconOrganizer implements BeaconConsumer, APIRequester.BeaconsRoomInfoListener {

    private static final String TAG = "BeaconOrganizer";
    private static final List<BeaconOrganizerListener> sListeners = new LinkedList<>();
    private static BeaconRoom mAvailableRoom;

    public static Integer attachListener(BeaconOrganizerListener listener) {
        sListeners.add(listener);
        return mAvailableRoom == null ? null : mAvailableRoom.getId();
    }

    public static void detachListener(BeaconOrganizerListener listener) {
        sListeners.remove(listener);
    }

    private final Context mContext;
    private final Bus mBus;
    private List<BeaconRoom> mRooms;

    private BeaconManager m_manager;
    boolean started;

    public BeaconOrganizer(Context context, Bus bus) {
        this.mContext = context;
        this.mBus = bus;
        APIRequester apiRequester = new APIRequester(mContext, bus);
        try {
            apiRequester.requestRoomsInfo(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        started = false;

        m_manager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(mContext);
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

    public void start() {
        if (!started) {
            Log.i(TAG, "Start " + TAG);
            mBus.register(this);
            started = true;
            m_manager.bind(this);
        }
    }

    public void stop() {
        if (started) {
            started = false;
            m_manager.unbind(this);
            mBus.unregister(this);
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        Region tmpRegion;
        m_manager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time ! " + region.getUniqueId());

                if (mAvailableRoom != null) {
                    if (Integer.toString(mAvailableRoom.getId()).compareTo(region.getUniqueId()) == 0) {
                        return;
                    }
                }

                for (BeaconRoom room : mRooms) {
                    if (Integer.toString(room.getId()).compareTo(region.getUniqueId()) == 0) {

                        if (mAvailableRoom != null) {
                            onRoomUnavailable();
                        }
                        mAvailableRoom = room;
                        onRoomAvailable(mAvailableRoom.getId());
                    }
                }
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "Exit of region : " + region.getUniqueId());
                if (mAvailableRoom != null) {
                    if (Integer.toString(mAvailableRoom.getId()).compareTo(region.getUniqueId()) == 0) {
                        onRoomUnavailable();
                        mAvailableRoom = null;
                    }
                }
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "Change state of region : " + region.getUniqueId() + " state : " + state);
                if (mAvailableRoom != null) {
                    if (Integer.toString(mAvailableRoom.getId()).compareTo(region.getUniqueId()) == 0) {
                        if (state == 0) {
                            onRoomUnavailable();
                            mAvailableRoom = null;
                        }
                        return;
                    }
                }

                if (state != 0) {
                    for (BeaconRoom room : mRooms) {
                        if (Integer.toString(room.getId()).compareTo(region.getUniqueId()) == 0) {

                            if (mAvailableRoom != null) {
                                onRoomUnavailable();
                            }
                            mAvailableRoom = room;
                            onRoomAvailable(mAvailableRoom.getId());
                        }
                    }
                }
            }
        });

        if (mRooms != null)
            Log.i(TAG, "rooms size is " + mRooms.size());
        else
            Log.i(TAG, "rooms is null");

        int i = 0;

        if (mRooms != null) {
            for (BeaconRoom r : mRooms) {
                for (Beacon b : r.getBeacons()) {
                    try {
                        tmpRegion = new Region("" + r.getId(), Identifier.parse(b.getUUID()), null, null);
                        m_manager.startMonitoringBeaconsInRegion(tmpRegion);

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

    private void onRoomAvailable(int roomId) {
        for (BeaconOrganizerListener listener : sListeners) {
            listener.onRoomAvailable(roomId);
        }
    }

    private void onRoomUnavailable() {
        for (BeaconOrganizerListener listener : sListeners) {
            listener.onRoomUnavailable();
        }
    }

    public static interface BeaconOrganizerListener {
        void onRoomAvailable(int roomId);

        void onRoomUnavailable();
    }

    @Override
    public void onBeaconsRoomInfoReceived(List<BeaconRoom> rooms) {
        this.mRooms = rooms;
    }

    @Override
    public Context getApplicationContext() {
        return mContext;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        mContext.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int flag) {
        return mContext.bindService(intent, serviceConnection, flag);
    }
}
