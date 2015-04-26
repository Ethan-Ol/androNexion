package com.nexion.tchatroom.model;

/**
 * Created by DarzuL on 26/04/2015.
 */
public class Room extends AbstractEntity {
    private final String name;

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
