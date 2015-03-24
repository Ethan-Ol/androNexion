package com.nexion.tchatroom;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.util.Log;

import dagger.ObjectGraph;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by DarzuL on 08/03/2015.
 */
public class App extends Application {
    private static final String TAG = "App";

    private ObjectGraph objectGraph;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        objectGraph = ObjectGraph.create(new AppModule(this));
        objectGraph.inject(this);
        objectGraph.injectStatics();

        //init();
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    private void init() {
        initBluetoothAlarm();
    }

    private void initBluetoothAlarm() {
        alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), BluetoothReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, 0, 1000 * 30, alarmIntent);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(BluetoothManager.getInstance().getBluetoothReceiver(), filter);
    }
}