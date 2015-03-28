package com.nexion.tchatroom.event;

import com.nexion.tchatroom.model.User;

/**
 * Created by ethan on 28/03/15.
 */
public class JoinReceivedEvent {
    private User user;

    public JoinReceivedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
