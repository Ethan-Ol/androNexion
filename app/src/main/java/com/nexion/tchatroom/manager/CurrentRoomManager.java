package com.nexion.tchatroom.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DarzuL on 27/03/2015.
 * <p/>
 * Manage the current roomId of the user
 */
public class CurrentRoomManager implements KeyFields, IManager<Integer> {
    private int roomId = 0;
    private final SharedPreferences sharedPreferences;

    public CurrentRoomManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        if (roomId == 0) {
            roomId = get();
        }
    }

    @Override
    public boolean isExist() {
        return roomId != 0;
    }

    @Override
    public void set(Integer roomId) {
        sharedPreferences
                .edit()
                .putInt(KEY_ROOM, roomId)
                .apply();
    }

    public Integer get() {
        if (roomId == 0) {
            roomId = sharedPreferences.getInt(KEY_ROOM, 0);
        }

        return roomId;
    }
}
