package com.nexion.tchatroom.event;

import com.nexion.tchatroom.model.Room;

/**
 * Created by ethan on 25/03/15.
 */
public class OnRoomUnavailableEvent {
    private Room mRoom;
    public OnRoomUnavailableEvent(Room r){mRoom = r;}

    public Room getRoom() {
        return mRoom;
    }
}
