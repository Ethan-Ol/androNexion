package com.nexion.tchatroom.api;

import com.nexion.tchatroom.model.Beacon;
import com.nexion.tchatroom.model.BeaconRoom;
import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by DarzuL on 14/03/2015.
 * <p/>
 * Convert JSON to java object
 */
abstract class JSONConverter implements JSONFields {

    static User jsonObjectToUser(JSONObject jsonObject) throws JSONException {
        return new User(jsonObject.getInt(FIELD_ID), jsonObject.getString(FIELD_PSEUDO), jsonObject.getInt(FIELD_ACL), jsonObject.getInt(FIELD_IN_ROOM) == User.IN_ROOM);
    }

    static NexionMessage jsonObjectToMessage(JSONObject jsonObject) throws JSONException {
        return new NexionMessage(jsonObject.getString(FIELD_CONTENT), jsonObject.getLong(FIELD_DATE), jsonObject.getInt(FIELD_AUTHOR_ID));
    }

    public static BeaconRoom jsonObjectToBeaconRoom(JSONObject jsonObject) throws JSONException {
        int roomId = jsonObject.getInt(FIELD_ID);
        String roomName = jsonObject.getString(FIELD_ROOM_NAME);

        List<Beacon> beacons = new LinkedList<>();
        JSONArray jsonArrayBeacons = jsonObject.getJSONArray(FIELD_BEACONS);
        int len = jsonArrayBeacons.length();
        for (int i = 0; i < len; i++) {
            JSONObject jsonObjectBeacon = jsonArrayBeacons.getJSONObject(i);
            Beacon beacon = jsonObjectToBeacon(jsonObjectBeacon, roomId);
            beacons.add(beacon);
        }

        return new BeaconRoom(roomId, roomName, beacons);
    }

    private static Beacon jsonObjectToBeacon(JSONObject jsonObject, int roomId) throws JSONException {
        return new Beacon(jsonObject.getInt(FIELD_ID), jsonObject.getString(FIELD_UUID), roomId);
    }
}
