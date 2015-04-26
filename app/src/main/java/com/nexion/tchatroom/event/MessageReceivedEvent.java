package com.nexion.tchatroom.event;

/**
 * Created by DarzuL on 15/03/2015.
 */
public class MessageReceivedEvent {
    private int authorId;
    private String content;
    private long dateTime;

    public MessageReceivedEvent(int authorId, String content, long dateTime) {
        this.authorId = authorId;
        this.content = content;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public long getDateTime() {
        return dateTime;
    }
}
