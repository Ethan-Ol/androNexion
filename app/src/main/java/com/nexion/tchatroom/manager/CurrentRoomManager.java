package com.nexion.tchatroom.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.nexion.tchatroom.model.Room;

/**
 * Created by DarzuL on 27/03/2015.
 *
 * Manage the current room of the user
 */
public class CurrentRoomManager implements KeyFields, IManager<Room> {
    private Room room;
    private final SharedPreferences sharedPreferences;

    public CurrentRoomManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        room = get();
    }

    @Override
    public boolean isExist() {
        return room != null;
    }

    @Override
    public void set(Room room) {
        sharedPreferences
                .edit()
                .putInt(KEY_ROOM, room.getId())
                .apply();
    }

    public Room get() {
        if (room == null) {
            room = new Room();
            room.setId(sharedPreferences.getInt(KEY_ROOM, 0));
        }

        return room;
    }
}
