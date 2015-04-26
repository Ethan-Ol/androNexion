package com.nexion.tchatroom.model;

public class User extends AbstractEntity {

    private final int ACL_TEACHER = 1;
    private final int ACL_STUDENT = 0;

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

    public boolean isAdmin() {
        return acl == ACL_TEACHER;
    }
}