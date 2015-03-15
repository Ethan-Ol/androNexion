package com.nexion.tchatroom.model;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Room extends AbstractEntity {
    String name;
    List<Beacon> beacons;
    List<User> users;
    List<NexionMessage> messages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Beacon> getBeacons() {
        return beacons;
    }

    public void setBeacons(List<Beacon> beacons) {
        this.beacons = beacons;
    }

    public List<NexionMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<NexionMessage> messages) {
        this.messages = messages;
    }

    public void addMessage(NexionMessage message) {
        messages.add(message);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int countMessages() {
        return messages.size();
    }
}
