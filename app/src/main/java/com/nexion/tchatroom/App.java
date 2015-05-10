package com.nexion.tchatroom;

import android.app.Application;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import dagger.ObjectGraph;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by DarzuL on 08/03/2015.
 */
public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    public static final boolean DEBUG = true;

    @Inject
    Bus bus;
    private ObjectGraph objectGraph;
    private BeaconOrganizer mBeaconOrganizer;

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

        mBeaconOrganizer = new BeaconOrganizer(this, bus);
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    public BeaconOrganizer getBeaconOrganizer() {
        return mBeaconOrganizer;
    }
}