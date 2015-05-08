package com.nexion.tchatroom.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.manager.KeyFields;
import com.nexion.tchatroom.model.BeaconRoom;
import com.nexion.tchatroom.model.ChatRoom;
import com.nexion.tchatroom.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by DarzuL on 14/03/2015.
 * <p/>
 * Methods to call API
 */
public class APIRequester {

    private static final String url = "http://git.ethandev.fr/API";

    private final RequestQueue queue;
    private final JSONFactory jsonFactory;

    private String token;

    public APIRequester(final Context context) {
        queue = Volley.newRequestQueue(context);
        token = context.getSharedPreferences(KeyFields.PREF_FILE, Context.MODE_PRIVATE)
                .getString(KeyFields.KEY_TOKEN, null);

        this.jsonFactory = new JSONFactory();
    }

    public void requestToken(String login, String password, final UserInfoListener listener) throws JSONException {
        String page = "/getToken.php";
        JSONObject jsonObject = jsonFactory.createLoginJSON(login, password);
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            token = JSONParser.getToken(response);
                            listener.onUserConnected(token, JSONParser.getUserInfo(response));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                listener));
    }

    public void requestRoomsInfo(final BeaconsRoomInfoListener listener) throws JSONException {
        String page = "/getInitDatas.php";
        JSONObject jsonObject = jsonFactory.createTokenJSON(token);
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listener.onBeaconsRoomInfoReceived(JSONParser.parseJSONBeaconRooms(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                listener));
    }

    public void postMessage(String content) throws JSONException {
        String page = "/postMsg.php";
        JSONObject jsonObject = jsonFactory.createMessageJSON(token, content);
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject, null, null));
    }

    public void joinRoom(final int roomId, String password, final RoomJoinListener listener) throws JSONException {
        String page = "/joinTchat.php";
        JSONObject jsonObject = jsonFactory.createRoomJSON(token, roomId, password);
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listener.onRoomJoined(JSONParser.parseJSONRoomResponse(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                listener));
    }

    public void leaveRoom() throws JSONException {
        String page = "/leaveTchat.php";
        JSONObject jsonObject = jsonFactory.createTokenJSON(token);
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject, null, null));
    }

    public void clearRoom() throws JSONException {
        String page = "/clearTchat.php";
        JSONObject jsonObject = jsonFactory.createTokenJSON(token);
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject, null, null));
    }

    public void kickUser(User user) throws JSONException {
        String page = "/kick.php";
        JSONObject jsonObject = jsonFactory.createUserJSON(token, user);
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject, null, null));
    }

    public void sendGcmKey(String regid) throws JSONException {
        String page = "/setDeviceId.php";
        JSONObject jsonObject = jsonFactory.createGcmJSON(token, regid);
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject,
                null,
                null));
    }

    public interface UserInfoListener extends Response.ErrorListener {
        void onUserConnected(String token, User user);
    }

    public interface RoomJoinListener extends Response.ErrorListener {
        void onRoomJoined(ChatRoom room);
    }

    public interface BeaconsRoomInfoListener extends Response.ErrorListener {
        void onBeaconsRoomInfoReceived(List<BeaconRoom> rooms);
    }
}
