package com.nexion.tchatroom.api;

/**
 * Created by DarzuL on 14/03/2015.
 * <p/>
 * Fields used to parse json respond from API
 */
interface JSONFields {

    String FIELD_ID = "id";

    String FIELD_LOGIN = "login";
    String FIELD_PASSWORD = "password";
    String FIELD_TOKEN = "token";

    String FIELD_USERS = "users";
    String FIELD_PSEUDO = "pseudo";
    String FIELD_ACL = "acl";
    String FIELD_KICK_DURATION = "duration";
    String FIELD_IN_ROOM = "in_room";

    String FIELD_MESSAGES = "messages";
    String FIELD_AUTHOR_ID = "author_id";
    String FIELD_CONTENT = "content";
    String FIELD_DATE = "date";

    String FIELD_ROOMS = "rooms";
    String FIELD_ROOM_ID = "room_id";

    String FIELD_BEACONS = "beacons";
    String FIELD_UUID = "UUID";

    String FIELD_GCM_KEY = "gcm_key";

    String FIELD_ROOM_NAME = "name";
}
