package com.nexion.tchatroom.model;

public class User extends AbstractEntity {

    public static final int ACL_TEACHER = 1;
    public static final int ACL_STUDENT = 0;
    public static int currentUserId;

    private String pseudo;
    private int acl;

    public User(int id, String pseudo, int acl) {
        this.id = id;
        this.pseudo = pseudo;
        this.acl = acl;
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
}