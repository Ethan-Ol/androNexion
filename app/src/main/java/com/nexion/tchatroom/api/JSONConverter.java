package com.nexion.tchatroom.api;

import com.nexion.tchatroom.model.Beacon;
import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.Room;
import com.nexion.tchatroom.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by DarzuL on 14/03/2015.
 */
public final class JSONConverter implements JSONFields {

    static User jsonObjectToUser(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.setPseudo(jsonObject.getString(FIELD_PSEUDO));
        user.setId(jsonObject.getInt(FIELD_ID));
        user.isAdmin(jsonObject.getInt(FIELD_ROLE) == 1);

        return user;
    }

    static NexionMessage jsonObjectToMessage(JSONObject jsonObject, List<User> users) throws JSONException {
        NexionMessage message = new NexionMessage();
        message.setContent(jsonObject.getString(FIELD_CONTENT));
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(jsonObject.getLong(FIELD_DATE) * 1000);
        message.setSendAt(date);

        int userId = jsonObject.getInt(FIELD_USER_ID);
        for (User user : users) {
            if (user.getId() == userId) {
                message.setAuthor(user);
                return message;
            }
        }

        return null;
    }

    public static Room jsonObjectToRoom(JSONObject jsonObject) throws JSONException {
        Room room = new Room();

        room.setId(jsonObject.getInt(FIELD_ID));

        List<Beacon> beacons = new LinkedList<>();
        JSONArray jsonArrayBeacons = jsonObject.getJSONArray(FIELD_BEACONS);
        int len = jsonArrayBeacons.length();
        for (int i = 0; i < len; i++) {
            JSONObject jsonObjectBeacon = jsonArrayBeacons.getJSONObject(i);
            Beacon beacon = jsonObjectToBeacon(jsonObjectBeacon, room);
            beacons.add(beacon);
        }
        room.setBeacons(beacons);
        room.setName(jsonObject.getString(FIELD_ROOM_NAME));

        return room;
    }

    public static Beacon jsonObjectToBeacon(JSONObject jsonObject, Room room) throws JSONException {
        Beacon beacon = new Beacon();

        beacon.setId(jsonObject.getInt(FIELD_ID));
        beacon.setUUID(jsonObject.getString(FIELD_UUID));
        beacon.setRoom(room);
        return beacon;
    }
}
