package com.nexion.tchatroom.event;

import com.nexion.tchatroom.model.Room;

/**
 * Created by DarzuL on 22/03/2015.
 */
public class OnRoomAvailableEvent {
    private Room mRoom;
    public OnRoomAvailableEvent(Room r){
        mRoom=r;
    }

    public Room getRoom() {
        return mRoom;
    }
}
