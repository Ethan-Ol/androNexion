package com.nexion.tchatroom.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nexion.tchatroom.activity.LoginActivity;
import com.nexion.tchatroom.api.APIRequester;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by DarzuL on 27/03/2015.
 */
public class PlayServicesManager {

    private static final String TAG = PlayServicesManager.class.getSimpleName();
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    private final String SENDER_ID = "520811926007";
    private final Context context;
    private final AtomicInteger msgId = new AtomicInteger();
    private GoogleCloudMessaging gcm;
    private String regid;
    private APIRequester apiRequester;


    public PlayServicesManager(Context context, APIRequester apiRequester) {
        this.context = context;
        this.apiRequester = apiRequester;
        gcm = GoogleCloudMessaging.getInstance(context);
        regid = getRegistrationId(context);

        if (regid.isEmpty()) {
            registerInBackground();
        }
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground() {
        new AsyncTask() {
            @Override
            protected String doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    sendRegistrationIdToBackend();

                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend() {
        try {
            apiRequester.sendGcmKey(regid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }
}