package com.nexion.tchatroom.event;

/**
 * Created by DarzuL on 22/03/2015.
 */
public class OnRoomAvailableEvent {
    private final int roomId;

    public OnRoomAvailableEvent(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomId() {
        return roomId;
    }
}
