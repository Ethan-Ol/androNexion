package com.nexion.tchatroom.model;

import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;

public class User extends AbstractEntity implements Serializable {

    private static final String STATE_PSEUDO = "pseudo";
    private static final String STATE_ACL = "acl";
    private static final String STATE_IN_ROOM = "in_room";

    public static final int ACL_TEACHER = 1;
    public static final int ACL_STUDENT = 0;
    public static final int IN_ROOM = 1;
    public static int currentUserId;

    private String pseudo;
    private int acl;
    private boolean inRoom;

    public User(int id, String pseudo, int acl, boolean inRoom) {
        this.id = id;
        this.pseudo = pseudo;
        this.acl = acl;
        this.inRoom = inRoom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public int getAcl() {
        return acl;
    }

    public boolean isAdmin() {
        return acl == ACL_TEACHER;
    }

    public boolean isInRoom() {
        return inRoom;
    }

    public Bundle save() {
        Bundle savedState = new Bundle();

        savedState.putString(STATE_PSEUDO, pseudo);
        savedState.putInt(STATE_ACL, acl);
        savedState.putBoolean(STATE_IN_ROOM, inRoom);

        return savedState;
    }
}