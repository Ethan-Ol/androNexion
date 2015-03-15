package com.nexion.tchatroom.api;

import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.Room;
import com.nexion.tchatroom.model.Token;
import com.nexion.tchatroom.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by DarzuL on 14/03/2015.
 */
public class JSONParser extends JSONFields {

    @Inject
    Token token;
    @Inject
    User user;
    @Inject
    List<Room> rooms;

    void parseJSONTokenResponse(JSONObject jsonObject) throws JSONException {
        String key = jsonObject.getString(FIELD_TOKEN);
        token.setKey(key);
    }

    public void parseJSONUserResponse(JSONObject response) throws JSONException {
        String pseudo = response.getString(FIELD_PSEUDO);
        boolean isAdmin = response.getBoolean(FIELD_ROLE);
        user.setPseudo(pseudo);
        user.isAdmin(isAdmin);
    }

    public void parseJSONRooms(JSONObject response) throws JSONException {
        JSONArray jsonArrayRooms = response.getJSONArray(FIELD_ROOMS);

        int len = jsonArrayRooms.length();
        for (int i=0; i<len; i++) {
            Room room = JSONConverter.jsonObjectToRoom(jsonArrayRooms.getJSONObject(i));
            rooms.add(room);
        }
    }

    public void parseJSONRoomResponse(JSONObject response, Room room) throws JSONException {
        JSONArray users = response.getJSONArray(FIELD_USERS);
        JSONArray messages = response.getJSONArray(FIELD_MESSAGES);

        room.setUsers(new LinkedList<User>());
        room.setMessages(new LinkedList<NexionMessage>());
        int len = users.length();
        for(int i=0; i < len; i++) {
            User user = JSONConverter.jsonObjectToUser(users.getJSONObject(i));
            room.getUsers().add(user);
        }

        len = messages.length();
        for(int i=0; i < len; i++) {
            NexionMessage message = JSONConverter.jsonObjectToMessage(messages.getJSONObject(i), room.getUsers());
            room.getMessages().add(message);
        }
    }
}
