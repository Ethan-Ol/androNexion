package com.nexion.tchatroom.event;

import com.nexion.tchatroom.model.User;

/**
 * Created by ethan on 28/03/15.
 */
public class LeaveReceivedEvent {
    public User getUser() {
        return user;
    }

    public LeaveReceivedEvent(User user) {

        this.user = user;
    }

    private User user;
}
