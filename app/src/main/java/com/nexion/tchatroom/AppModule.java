package com.nexion.tchatroom;

import android.content.Context;

import com.nexion.beaconManagment.BeaconOrganizer;
import com.nexion.beaconManagment.Main2Activity;
import com.nexion.tchatroom.activity.ChatRoomActivity;
import com.nexion.tchatroom.activity.KickActivity;
import com.nexion.tchatroom.activity.LoginActivity;
import com.nexion.tchatroom.activity.WaitingRoomActivity;
import com.nexion.tchatroom.api.APIRequester;
import com.nexion.tchatroom.api.JSONFactory;
import com.nexion.tchatroom.api.JSONParser;
import com.nexion.tchatroom.fragment.ChatRoomFragment;
import com.nexion.tchatroom.fragment.KickFragment;
import com.nexion.tchatroom.fragment.LoginFragment;
import com.nexion.tchatroom.fragment.WaitingRoomFragment;
import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.Room;
import com.nexion.tchatroom.model.User;
import com.squareup.otto.Bus;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DarzuL on 09/03/2015.
 */
@Module(
        staticInjections = NexionMessage.class,

        injects = {
                App.class,
                LoginActivity.class,
                WaitingRoomActivity.class,
                ChatRoomActivity.class,
                KickActivity.class,
                LoginFragment.class,
                WaitingRoomFragment.class,
                APIRequester.class,
                JSONParser.class,
                JSONFactory.class,
                Main2Activity.class,
                BluetoothReceiver.class,
                BeaconOrganizer.class,
                ScanService.class
        }
)
public class AppModule {
    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return app;
    }

    @Provides
    @Singleton
    public User provideCurrentUser() {
        return new User("", false);
    }

    @Provides
    @Singleton
    public Room provideRoom() {
        return new Room();
    }

    @Provides
    @Singleton
    public List<Room> provideRooms() {
        return new LinkedList<>();
    }

    @Provides
    @Singleton
    public Bus provideBus() {
        return new AndroidBus();
    }

        /*    @Provides
        @Singleton
        public Room provideCurrentRoom() {
            return null;
        }
    */
}
