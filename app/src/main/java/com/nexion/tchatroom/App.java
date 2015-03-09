package com.nexion.tchatroom;

import android.app.Application;

import dagger.ObjectGraph;

/**
 * Created by DarzuL on 08/03/2015.
 */
public class App extends Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        objectGraph = ObjectGraph.create(new AppModule(this));
        objectGraph.inject(this);
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }
}
