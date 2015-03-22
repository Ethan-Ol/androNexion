package com.nexion.tchatroom.model;

public class User extends AbstractEntity {

    private String pseudo;
    private boolean isAdmin;

    public User() {

    }

    public User(String pseudo, boolean isAdmin) {
        this.pseudo = pseudo;
        this.isAdmin = isAdmin;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void isAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean notExist() {
        return pseudo.isEmpty();
    }
}