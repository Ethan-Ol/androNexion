package com.nexion.tchatroom.api;

import com.nexion.tchatroom.model.BeaconRoom;
import com.nexion.tchatroom.model.ChatRoom;
import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by DarzuL on 14/03/2015.
 *
 * Transform json to java object
 */
public class JSONParser implements JSONFields {

    static String getToken(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(FIELD_TOKEN);
    }

    static User getUserInfo(JSONObject response) throws JSONException {
        return new User(response.getInt(FIELD_ID), response.getString(FIELD_PSEUDO), response.getInt(FIELD_ACL), false);
    }

    static List<BeaconRoom> parseJSONBeaconRooms(JSONObject response) throws JSONException {
        JSONArray jsonArrayRooms = response.getJSONArray(FIELD_ROOMS);

        List<BeaconRoom> rooms = new LinkedList<>();
        int len = jsonArrayRooms.length();
        for (int i = 0; i < len; i++) {
            BeaconRoom room = JSONConverter.jsonObjectToBeaconRoom(jsonArrayRooms.getJSONObject(i));
            rooms.add(room);
        }

        return rooms;
    }

    static ChatRoom parseJSONRoomResponse(int roomId, JSONObject response) throws JSONException {
        String roomName = response.getString(FIELD_ROOM_NAME);
        JSONArray messagesJSONArray = response.getJSONArray(FIELD_MESSAGES);
        JSONArray usersJSONArray = response.getJSONArray(FIELD_USERS);

        Map<Integer, User> userMap = new HashMap<>();
        List<NexionMessage> messages = new LinkedList<>();
        int len = usersJSONArray.length();
        for (int i = 0; i < len; i++) {
            User user = JSONConverter.jsonObjectToUser(usersJSONArray.getJSONObject(i));
            userMap.put(user.getId(), user);
        }

        len = messagesJSONArray.length();
        for (int i = 0; i < len; i++) {
            NexionMessage message = JSONConverter.jsonObjectToMessage(messagesJSONArray.getJSONObject(i));

            messages.add(message);
        }

        ChatRoom chatRoom = new ChatRoom(roomId, roomName, userMap, messages);
        ChatRoom.compute(chatRoom);

        return chatRoom;
    }
}
