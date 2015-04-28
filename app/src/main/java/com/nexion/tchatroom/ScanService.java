package com.nexion.tchatroom;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.nexion.beaconManagment.BeaconOrganizer;

import javax.inject.Inject;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class ScanService extends IntentService {
    private static final String ACTION_START = "com.nexion.tchatroom.action.START";
    private static final String ACTION_STOP = "com.nexion.tchatroom.action.STOP";

    private final BeaconOrganizer beaconOrganizer;

    public static void startActionScan(Context context) {
        Intent intent = new Intent(context, ScanService.class);
        intent.setAction(ACTION_START);
        context.startService(intent);
    }

    public static void stopActionScan(Context context) {
        Intent intent = new Intent(context, ScanService.class);
        intent.setAction(ACTION_STOP);
        context.startService(intent);
    }

    public ScanService() {
        super("ScanService");
        beaconOrganizer = new BeaconOrganizer(getApplicationContext());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ((App) getApplication()).inject(this);
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                handleStartScan();
            } else if (ACTION_STOP.equals(action)) {
                handleStopScan();
            }
        }
    }

    private void handleStartScan() {
        beaconOrganizer.start();
    }

    private void handleStopScan() {
        beaconOrganizer.stop();
    }
}
