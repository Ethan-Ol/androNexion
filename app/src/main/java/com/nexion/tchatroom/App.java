package com.nexion.tchatroom;

import android.app.Application;

import com.nexion.beaconManagment.BeaconOrganizer;

import dagger.ObjectGraph;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by DarzuL on 08/03/2015.
 */
public class App extends Application {
    private static final String TAG = "App";

    private ObjectGraph objectGraph;
    private BeaconOrganizer mBeaconOrganizer;
    private BeaconOrganizer beaconOrganizer;

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        objectGraph = ObjectGraph.create(new AppModule());
        objectGraph.inject(this);
        objectGraph.injectStatics();

        mBeaconOrganizer = new BeaconOrganizer(this);
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    public BeaconOrganizer getBeaconOrganizer() {
        return beaconOrganizer;
    }
}