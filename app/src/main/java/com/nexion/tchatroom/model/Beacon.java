package com.nexion.tchatroom.model;


public class Beacon extends AbstractEntity {
    String UUID;
    Room room;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
