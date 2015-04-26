package com.nexion.tchatroom.event;

/**
 * Created by ethan on 25/03/15.
 */
public class OnRoomUnavailableEvent {
    private final int roomId;

    public OnRoomUnavailableEvent(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomId() {
        return roomId;
    }
}
