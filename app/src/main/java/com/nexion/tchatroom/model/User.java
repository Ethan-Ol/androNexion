package com.nexion.tchatroom.model;

public class User extends AbstractEntity {

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
}