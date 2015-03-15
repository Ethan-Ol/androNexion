package com.nexion.tchatroom.api;

import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.Room;
import com.nexion.tchatroom.model.Token;
import com.nexion.tchatroom.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Created by DarzuL on 14/03/2015.
 */
public class JSONFactory extends JSONFields {

    @Inject
    Token token;

    JSONObject createLoginJSON(String login, String password) throws JSONException {
        String str = "{"
                + "\"" + FIELD_LOGIN + "\"" + ":" + login + ","
                + "\"" + FIELD_PASSWORD + "\"" + ":" + password
                + "}";

        return new JSONObject(str);
    }

    JSONObject createTokenJSON() throws JSONException {
        String str = "{"
                + "\"" + FIELD_TOKEN + "\"" + ":" + token.getKey()
                + "}";

        return new JSONObject(str);
    }

    public JSONObject createMessageJSON(String content) throws JSONException {
        String str = "{"
                + "\"" + FIELD_TOKEN + "\"" + ":" + token.getKey() + ","
                + "\"" + FIELD_CONTENT + "\"" + ":" + content
                + "}";

        return new JSONObject(str);
    }

    public JSONObject createUserJSON(User user) throws JSONException {
        String str = "{"
                + "\"" + FIELD_TOKEN + "\"" + ":" + token.getKey() + ","
                + "\"" + FIELD_USER_ID + "\"" + ":" + user.getId() + ","
                + "\"" + FIELD_KICK_DURATION + "\"" + ":" + 60
                + "}";

        return new JSONObject(str);
    }

    public JSONObject createRoomJSON(Room room, String password) throws JSONException {
        String str = "{"
                + "\"" + FIELD_TOKEN + "\"" + ":" + token.getKey() + ","
                + "\"" + FIELD_ROOM_ID + "\"" + ":" + room.getId() + ","
                + "\"" + FIELD_PASSWORD + "\"" + ":" + password
                + "}";

        return new JSONObject(str);
    }
}
