package com.nexion.tchatroom.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nexion.tchatroom.event.RoomJoinedEvent;
import com.nexion.tchatroom.event.RoomsInfoReceivedEvent;
import com.nexion.tchatroom.event.TokenReceivedEvent;
import com.nexion.tchatroom.event.UserInfoReceivedEvent;
import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.Room;
import com.nexion.tchatroom.model.Token;
import com.nexion.tchatroom.model.User;
import com.squareup.otto.Bus;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Created by DarzuL on 14/03/2015.
 */
public class APIRequester {

    private final String url = "api.com";

    @Inject
    Token token;
    @Inject
    User user;
    @Inject
    Bus bus;

    RequestQueue queue;
    JSONFactory jsonFactory;
    JSONParser jsonParser;

    @Inject
    public APIRequester(Context context) {
        queue = Volley.newRequestQueue(context);
        jsonFactory = new JSONFactory();
        jsonParser = new JSONParser();
    }

    public void requestToken(String login, String password) throws JSONException {
        JSONObject jsonObject = jsonFactory.createLoginJSON(login, password);
        queue.add(new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonParser.parseJSONTokenResponse(response);
                            bus.post(new TokenReceivedEvent());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }));
    }

    public void requestUserInfo() throws JSONException {
        JSONObject jsonObject = jsonFactory.createTokenJSON();
        queue.add(new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonParser.parseJSONUserResponse(response);
                            bus.post(new UserInfoReceivedEvent());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }));
    }

    public void postMessage(String content) throws JSONException {
        JSONObject jsonObject = jsonFactory.createMessageJSON(content);
        queue.add(new JsonObjectRequest(Request.Method.POST, url, jsonObject, null, null));
    }

    public void joinRoom(final Room room, String password) throws JSONException {
        JSONObject jsonObject = jsonFactory.createRoomJSON(room, password);
        queue.add(new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonParser.parseJSONRoomResponse(response, room);
                            bus.post(new RoomJoinedEvent());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }));
    }

    public void leaveRoom() throws JSONException {
        JSONObject jsonObject = jsonFactory.createTokenJSON();
        queue.add(new JsonObjectRequest(Request.Method.POST, url, jsonObject, null, null));
    }

    public void clearRoom() throws JSONException {
        JSONObject jsonObject = jsonFactory.createTokenJSON();
        queue.add(new JsonObjectRequest(Request.Method.POST, url, jsonObject, null, null));
    }

    public void leaveRoom(User user) throws JSONException {
        JSONObject jsonObject = jsonFactory.createUserJSON(user);
        queue.add(new JsonObjectRequest(Request.Method.POST, url, jsonObject, null, null));
    }

    public void requestRoomsInfo() throws JSONException {
        JSONObject jsonObject = jsonFactory.createTokenJSON();
        queue.add(new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonParser.parseJSONRooms(response);
                            bus.post(new RoomsInfoReceivedEvent());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }));
    }
}
