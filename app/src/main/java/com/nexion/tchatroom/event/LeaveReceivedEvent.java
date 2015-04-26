package com.nexion.tchatroom.event;

/**
 * Created by ethan on 28/03/15.
 */
public class LeaveReceivedEvent {

    private int userId;

    public int getUserId() {
        return userId;
    }

    public LeaveReceivedEvent(int userId) {

        this.userId = userId;
    }
}
