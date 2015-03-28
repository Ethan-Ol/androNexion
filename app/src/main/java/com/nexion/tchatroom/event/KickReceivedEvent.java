package com.nexion.tchatroom.event;

import com.nexion.tchatroom.model.User;

/**
 * Created by DarzuL on 28/03/2015.
 */
public class KickReceivedEvent {

    User user;

    public KickReceivedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
