package com.nexion.tchatroom.event;

import com.nexion.tchatroom.model.NexionMessage;

/**
 * Created by DarzuL on 15/03/2015.
 */
public class MessageReceivedEvent {
    private NexionMessage message;

    public MessageReceivedEvent(NexionMessage message) {
        this.message = message;
    }

    public NexionMessage getMessage() {
        return message;
    }
}
