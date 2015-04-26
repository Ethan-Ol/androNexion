package com.nexion.tchatroom.event;

/**
 * Created by DarzuL on 28/03/2015.
 */
public class KickReceivedEvent {

    int userId;

    public KickReceivedEvent(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
