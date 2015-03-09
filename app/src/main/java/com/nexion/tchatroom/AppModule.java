package com.nexion.tchatroom;

import android.content.Context;

import com.nexion.tchatroom.activity.MainActivity;
import com.nexion.tchatroom.model.Token;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DarzuL on 09/03/2015.
 */
@Module(
        injects = {
                App.class,
                MainActivity.class,
                Token.class
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
}
