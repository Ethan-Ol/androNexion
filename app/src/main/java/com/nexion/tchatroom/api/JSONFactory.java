package com.nexion.tchatroom.api;

import android.content.Context;

import com.nexion.tchatroom.manager.TokenManager;
import com.nexion.tchatroom.model.Room;
import com.nexion.tchatroom.model.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DarzuL on 14/03/2015.
 */
public class JSONFactory implements JSONFields {

    private final TokenManager tokenManager;

    public JSONFactory(Context context) {
        tokenManager = new TokenManager(context);
    }

    JSONObject createLoginJSON(String login, String password) throws JSONException {
        String str = "{"
                + "\"" + FIELD_LOGIN + "\"" + ":" + login + ","
                + "\"" + FIELD_PASSWORD + "\"" + ":" + password
                + "}";

        return new JSONObject(str);
    }

    JSONObject createTokenJSON() throws JSONException {
        String str = "{"
                + "\"" + FIELD_TOKEN + "\"" + ":" + tokenManager.get()
                + "}";

        return new JSONObject(str);
    }

    JSONObject createMessageJSON(String content) throws JSONException {
        String str = "{"
                + "\"" + FIELD_TOKEN + "\"" + ":" + tokenManager.get() + ","
                + "\"" + FIELD_CONTENT + "\"" + ":" + content
                + "}";

        return new JSONObject(str);
    }

    JSONObject createUserJSON(User user) throws JSONException {
        String str = "{"
                + "\"" + FIELD_TOKEN + "\"" + ":" + tokenManager.get() + ","
                + "\"" + FIELD_USER_ID + "\"" + ":" + user.getId() + ","
                + "\"" + FIELD_KICK_DURATION + "\"" + ":" + 60
                + "}";

        return new JSONObject(str);
    }

    JSONObject createRoomJSON(Room room, String password) throws JSONException {
        String str = "{"
                + "\"" + FIELD_TOKEN + "\"" + ":" + tokenManager.get() + ","
                + "\"" + FIELD_ROOM_ID + "\"" + ":" + room.getId() + ","
                + "\"" + FIELD_PASSWORD + "\"" + ":" + password
                + "}";

        return new JSONObject(str);
    }

    JSONObject createGcmJSON(String regid) throws JSONException {
        String str = "{"
                + "\"" + FIELD_TOKEN + "\"" + ":" + tokenManager.get() + ","
                + "\"" + FIELD_GCM_KEY + "\"" + ":" + regid
                + "}";

        return new JSONObject(str);
    }
}
