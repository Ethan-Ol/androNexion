package com.nexion.tchatroom.model;


public class Beacon extends AbstractEntity {
    private String UUID;
    private int roomId;

    public Beacon(int id, String UUID, int roomId) {
        this.id = id;
        this.UUID = UUID;
        this.roomId = roomId;
    }

    public String getUUID() {
        return UUID;
    }

    public int getRoomId() {
        return roomId;
    }
}
