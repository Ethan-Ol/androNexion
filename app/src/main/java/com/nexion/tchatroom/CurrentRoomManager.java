package com.nexion.tchatroom;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DarzuL on 27/03/2015.
 */
public class CurrentRoomManager {

    private static final String KEY_FILE = "nexion";
    private static final String KEY_ROOM = "current_room_id";

    private final SharedPreferences sharedPreferences;

    public CurrentRoomManager(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE);
    }

    public boolean isExist() {
        return sharedPreferences.getInt(KEY_ROOM, 0) != 0;
    }

    public void set(int roomId) {
        sharedPreferences
                .edit()
                .putInt(KEY_ROOM, roomId)
                .apply();
    }

    public int get() {
        return sharedPreferences.getInt(KEY_ROOM, 0);
    }
}
