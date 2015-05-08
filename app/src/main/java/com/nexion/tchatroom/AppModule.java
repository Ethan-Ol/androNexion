package com.nexion.tchatroom;

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
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DarzuL on 09/03/2015.
 * <p/>
 * The main module
 */
@Module(
        injects = {
                App.class,
                LoginActivity.class,
                WaitingRoomActivity.class,
                ChatRoomActivity.class,
                KickActivity.class,
                LoginFragment.class,
                WaitingRoomFragment.class,
                ChatRoomFragment.class,
                KickFragment.class,
                APIRequester.class,
                JSONParser.class,
                JSONFactory.class,
                BluetoothReceiver.class,
                BeaconOrganizer.class,
                ScanService.class,
                PushService.class
        }
)
public class AppModule {
    @Provides
    @Singleton
    public Bus provideBus() {
        return new AndroidBus();
    }
}
