package com.nexion.tchatroom.model;


import com.nexion.tchatroom.manager.CurrentUserManager;

import java.util.Calendar;

import javax.inject.Inject;

/**
 * Created by DarzuL on 08/03/2015.
 * <p/>
 * There is 3 item type
 * 0 -> Message from current user
 * 1 -> Message from other student
 * 2 -> Message from teacher
 * 3 -> Message from bot
 */
public class NexionMessage extends AbstractEntity {

    public static final int MESSAGE_FROM_USER = 0;
    public static final int MESSAGE_FROM_TEACHER = 1;
    public static final int MESSAGE_FROM_STUDENT = 2;
    public static final int MESSAGE_FROM_BOT = 3;

    int type;
    String content;
    Calendar sendAt;
    User author;

    public NexionMessage() {
    }

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

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}