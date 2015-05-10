package com.nexion.tchatroom.model;


import java.util.Calendar;

/**
 * Created by DarzuL on 08/03/2015.
 * <p/>
 * A message
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

    private String content;
    private Calendar sendAt;
    private Integer authorId;
    private int type;
    private boolean pending;

    public NexionMessage(String content, Long dateTime, Integer authorId) {
        this(content, dateTime, authorId, null);
    }

    public NexionMessage(String content, Long dateTime, Integer authorId, Integer type) {
        this.content = content;

        Calendar sendAt = null;
        if (dateTime == null) {
            pending = true;
        } else {
            sendAt = Calendar.getInstance();
            sendAt.setTimeInMillis(dateTime);
        }
        this.sendAt = sendAt;
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public Calendar getSendAt() {
        return sendAt;
    }

    public int getAuthorId() {
        return authorId;
    }

    public boolean isPending() {
        return pending;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void unpending(Calendar sendAt) {
        if (pending) {
            this.sendAt = sendAt;
        }
    }
}