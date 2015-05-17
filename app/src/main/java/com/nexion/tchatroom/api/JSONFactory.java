package com.nexion.tchatroom.api;

import com.nexion.tchatroom.model.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DarzuL on 14/03/2015.
 * <p/>
 * Factory to create json to communicate with the API
 */
public class JSONFactory implements JSONFields {

    JSONObject createLoginJSON(String login, String password) throws JSONException {
        String str = "{"
                + "\"" + FIELD_LOGIN + "\"" + ":" + "\"" + login + "\"" + ","
                + "\"" + FIELD_PASSWORD + "\"" + ":" + "\"" + password + "\""
                + "}";

        return new JSONObject(str);
    }

    JSONObject createTokenJSON(String token) throws JSONException {
        String str = "{"
                + "\"" + FIELD_TOKEN + "\"" + ":" + "\"" + token + "\""
                + "}";

        return new JSONObject(str);
    }

    JSONObject createMessageJSON(String token, String content) throws JSONException {
        String str = "{"
                + "\"" + FIELD_TOKEN + "\"" + ":" + "\"" + token + "\"" + ","
                + "\"" + FIELD_CONTENT + "\"" + ":" + "\"" + content + "\""
                + "}";

        return new JSONObject(str);
    }

    JSONObject createUserJSON(String token, User user) throws JSONException {
        String str = "{"
                + "\"" + FIELD_TOKEN + "\"" + ":" + "\"" + token + "\"" + ","
                + "\"" + FIELD_AUTHOR_ID + "\"" + ":" + user.getId() + ","
                + "\"" + FIELD_KICK_DURATION + "\"" + ":" + 60
                + "}";

        return new JSONObject(str);
    }

    JSONObject createRoomJSON(String token, int roomId, String password) throws JSONException {
        String str = "{"
                + "\"" + FIELD_TOKEN + "\"" + ":" + "\"" + token + "\"" + ","
                + "\"" + FIELD_ROOM_ID + "\"" + ":" + roomId + ","
                + "\"" + FIELD_PASSWORD + "\"" + ":" + "\"" + password + "\""
                + "}";

        return new JSONObject(str);
    }

    JSONObject createGcmJSON(String token, String regid) throws JSONException {
        String str = "{"
                + "\"" + FIELD_TOKEN + "\"" + ":" + token + ","
                + "\"" + FIELD_GCM_KEY + "\"" + ":" + regid
                + "}";

        return new JSONObject(str);
    }
}
