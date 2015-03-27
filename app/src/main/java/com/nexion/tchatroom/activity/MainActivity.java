package com.nexion.tchatroom.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.nexion.beaconManagment.BeaconOrganizer;
import com.nexion.tchatroom.App;
import com.nexion.tchatroom.BluetoothManager;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.api.APIRequester;
import com.nexion.tchatroom.event.BluetoothEnabledEvent;
import com.nexion.tchatroom.event.LoadingEvent;
import com.nexion.tchatroom.fragment.ChatRoomFragment;
import com.nexion.tchatroom.fragment.KickFragment;
import com.nexion.tchatroom.fragment.LoginFragment;
import com.nexion.tchatroom.fragment.WaitingRoomFragment;
import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.Room;
import com.nexion.tchatroom.model.Token;
import com.nexion.tchatroom.model.User;
import com.squareup.otto.Bus;

import org.json.JSONException;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends FragmentActivity implements
        LoginFragment.OnFragmentInteractionListener,
        ChatRoomFragment.OnFragmentInteractionListener,
        KickFragment.OnFragmentInteractionListener {


    private static final String TAG = "MainActivity";

    @Inject
    APIRequester apiRequester;

    @Inject
    Bus bus;

    @Inject
    User user;

    @Inject
    BeaconOrganizer beaconOrganizer;

    int currentRoomId;
    private boolean needToSendGcmKey = false;

    private boolean test = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((App) getApplication()).inject(this);
        context = getApplicationContext();

            //checkBluetooth();

            if (savedInstanceState == null) {

                if (test)
                    test();
                else {
                    if (token.isEmpty()) {
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.container, LoginFragment.newInstance(null), LoginFragment.TAG)
                                .commit();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.container, WaitingRoomFragment.newInstance(), WaitingRoomFragment.TAG)
                                .commit();
                        onTokenReceived(null);
                        bus.post(new LoadingEvent());
                    }
                }
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }
    @Override

    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    public void sendMessage(String content) {
        try {
            apiRequester.postMessage(content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void leaveRoom() {
        try {
            apiRequester.leaveRoom();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ChatRoomFragment.TAG);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    @Override
    public void showKickFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, KickFragment.newInstance(currentRoomId), KickFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onKick(List<User> userSelected) {
        for (User user : userSelected) {
            try {
                apiRequester.kickUser(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        onBackPressed();
    }

    @Inject
    List<Room> rooms;

    private void test() {
        user.setPseudo("DarzuL");
        User teacher = new User("Teacher", true);
        User student = new User("Student", false);

        List<User> users = new LinkedList<>();
        users.add(user);
        users.add(teacher);
        users.add(student);

        currentRoomId = 1;
        Room room = new Room();
        room.setId(1);
        room.setName("Room 1");
        room.setUsers(users);
        room.setMessages(new LinkedList<NexionMessage>());
        rooms.add(room);

        Calendar calendar = Calendar.getInstance();

        NexionMessage msg = new NexionMessage();
        msg.setAuthor(teacher);
        msg.setContent("Hey this is the teacher !");
        msg.setSendAt(calendar);
        room.addMessage(msg);

        msg = new NexionMessage();
        msg.setAuthor(student);
        msg.setContent("I am a student !");
        msg.setSendAt(calendar);
        room.addMessage(msg);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, ChatRoomFragment.newInstance(room), ChatRoomFragment.TAG)
                .commit();
    }
}
