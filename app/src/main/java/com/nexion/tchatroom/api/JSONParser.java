package com.nexion.tchatroom.api;

import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.Room;
import com.nexion.tchatroom.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by DarzuL on 14/03/2015.
 */
public class JSONParser implements JSONFields {

    static String parseJSONTokenResponse(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(FIELD_TOKEN);
    }

    static User parseJSONUserResponse(JSONObject response) throws JSONException {
        String pseudo = response.getString(FIELD_PSEUDO);
        boolean isAdmin = response.getInt(FIELD_ROLE) >= 1;

        return new User(pseudo, isAdmin);
    }

    static List<Room> parseJSONRooms(JSONObject response) throws JSONException {
        JSONArray jsonArrayRooms = response.getJSONArray(FIELD_ROOMS);

        List<Room> rooms = new LinkedList<>();
        int len = jsonArrayRooms.length();
        for (int i = 0; i < len; i++) {
            Room room = JSONConverter.jsonObjectToRoom(jsonArrayRooms.getJSONObject(i));
            rooms.add(room);
        }

        return rooms;
    }

    static Room parseJSONRoomResponse(JSONObject response) throws JSONException {
        JSONArray users = response.getJSONArray(FIELD_USERS);
        JSONArray messages = response.getJSONArray(FIELD_MESSAGES);

        Room room = new Room();
        room.setUsers(new LinkedList<User>());
        room.setMessages(new LinkedList<NexionMessage>());
        int len = users.length();
        for (int i = 0; i < len; i++) {
            User user = JSONConverter.jsonObjectToUser(users.getJSONObject(i));
            room.getUsers().add(user);
        }

        len = messages.length();
        for (int i = 0; i < len; i++) {
            NexionMessage message = JSONConverter.jsonObjectToMessage(messages.getJSONObject(i), room.getUsers());
            room.getMessages().add(message);
        }

        return room;
    }
}
