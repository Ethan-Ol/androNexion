package com.nexion.tchatroom.model;


public class Beacon extends AbstractEntity {
    String UUID;
    int roomId;

    public Beacon(int id, String UUID, int roomId) {
        this.id = id;
        this.UUID = UUID;
        this.roomId = roomId;
    }

    public String getUUID() {
        return UUID;
    }

    public int getRoom() {
        return roomId;
    }
}
