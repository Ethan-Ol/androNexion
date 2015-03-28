package com.nexion.tchatroom.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nexion.tchatroom.event.RequestFailedEvent;
import com.nexion.tchatroom.event.RoomJoinedEvent;
import com.nexion.tchatroom.event.RoomsInfoReceivedEvent;
import com.nexion.tchatroom.event.TokenReceivedEvent;
import com.nexion.tchatroom.event.UserInfoReceivedEvent;
import com.nexion.tchatroom.manager.CurrentRoomManager;
import com.nexion.tchatroom.manager.CurrentUserManager;
import com.nexion.tchatroom.manager.TokenManager;
import com.nexion.tchatroom.model.Room;
import com.nexion.tchatroom.model.User;
import com.squareup.otto.Bus;

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

    private static RequestQueue queue;
    private JSONFactory jsonFactory;
    private final Bus bus;
    private List<Room> rooms;

    private final CurrentRoomManager currentRoomManager;
    private final TokenManager tokenManager;
    private final CurrentUserManager currentUserManager;

    public APIRequester(final Context context, final Bus bus, List<Room> rooms) {
        this.bus = bus;
        this.jsonFactory = new JSONFactory(context);
        queue = Volley.newRequestQueue(context);
        this.rooms = rooms;

        currentRoomManager = new CurrentRoomManager(context);
        tokenManager = new TokenManager(context);
        currentUserManager = new CurrentUserManager(context);

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
                if (error.networkResponse != null)
                    bus.post(new RequestFailedEvent(context, error.networkResponse.statusCode));
            }
        };
    }

    Response.ErrorListener errorListener;

    public void requestToken(String login, String password) throws JSONException {
        String page = "/getToken.php";
        JSONObject jsonObject = jsonFactory.createLoginJSON(login, password);
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tokenManager.set(JSONParser.parseJSONTokenResponse(response));
                            bus.post(new TokenReceivedEvent());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                errorListener));
    }

    public void requestUserInfo() throws JSONException {
        String page = "/getCurrentUser.php";
        JSONObject jsonObject = jsonFactory.createTokenJSON();
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            currentUserManager.set(JSONParser.parseJSONUserResponse(response));
                            bus.post(new UserInfoReceivedEvent());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                errorListener));
    }

    public void requestRoomsInfo() throws JSONException {
        String page = "/getInitDatas.php";
        JSONObject jsonObject = jsonFactory.createTokenJSON();
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            rooms.addAll(JSONParser.parseJSONRooms(response));
                            bus.post(new RoomsInfoReceivedEvent());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                errorListener));
    }

    public void postMessage(String content) throws JSONException {
        String page = "/postMsg.php";
        JSONObject jsonObject = jsonFactory.createMessageJSON(content);
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject, null, null));
    }

    public void joinRoom(final Room room, String password) throws JSONException {
        String page = "/joinTchat.php";
        JSONObject jsonObject = jsonFactory.createRoomJSON(room, password);
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            currentRoomManager.set(JSONParser.parseJSONRoomResponse(response).getId());
                            bus.post(new RoomJoinedEvent());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                errorListener));
    }

    public void leaveRoom() throws JSONException {
        String page = "/leaveTchat.php";
        JSONObject jsonObject = jsonFactory.createTokenJSON();
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject, null, null));
    }

    public void clearRoom() throws JSONException {
        String page = "/clearTchat.php";
        JSONObject jsonObject = jsonFactory.createTokenJSON();
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject, null, null));
    }

    public void kickUser(User user) throws JSONException {
        String page = "/kick.php";
        JSONObject jsonObject = jsonFactory.createUserJSON(user);
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject, null, null));
    }

    public void sendGcmKey(String regid) throws JSONException {
        String page = "/setDeviceId.php";
        JSONObject jsonObject = jsonFactory.createGcmJSON(regid);
        queue.add(new JsonObjectRequest(Request.Method.POST, url + page, jsonObject,
                null,
                null));
    }
}
