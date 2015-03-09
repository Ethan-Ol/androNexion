package com.nexion.tchatroom.model;

import java.util.Calendar;

/**
 * Created by DarzuL on 08/03/2015.
 */
public class NexionMessage extends AbstractEntity {
    String content;
    Calendar sendAt;
    User author;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Calendar getSendAt() {
        return sendAt;
    }

    public void setSendAt(Calendar sendAt) {
        this.sendAt = sendAt;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
