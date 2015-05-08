package com.nexion.tchatroom.model;

import java.util.List;
import java.util.Map;

public class ChatRoom extends Room {
    private final Map<Integer, User> userMap;
    private List<NexionMessage> messages;

    public ChatRoom(int id, String name, Map<Integer, User> userMap, List<NexionMessage> messages) {
        super(id, name);
        this.userMap = userMap;
        this.messages = messages;
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

    public Map<Integer, User> getUsers() {
        return userMap;
    }

    public int countMessages() {
        return messages.size();
    }

    public void addUser(User user) {
        userMap.put(user.getId(), user);
    }

    public void removeUser(int userId) {
        userMap.remove(userId);
    }

    public NexionMessage getMessage(int position) {
        return messages.get(position);
    }

    public User getUser(int authorId) {
        return userMap.get(authorId);
    }
}
