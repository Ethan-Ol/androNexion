package com.nexion.tchatroom.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.api.APIRequester;
import com.nexion.tchatroom.fragment.KickFragment;
import com.nexion.tchatroom.model.User;
import com.squareup.otto.Bus;

import org.json.JSONException;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by DarzuL on 27/03/2015.
 * <p/>
 * Kick activity
 */
public class KickActivity extends BaseActivity implements KickFragment.OnFragmentInteractionListener {

    private final static String KICK_FRAGMENT_TAG = "ChatRoom";
    private APIRequester apiRequester;

    @Inject
    Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ((App) getApplication()).inject(this);

        apiRequester = new APIRequester(getApplicationContext());

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(KICK_FRAGMENT_TAG);
        if (fragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, KickFragment.newInstance(), KICK_FRAGMENT_TAG)
                    .commit();
        }
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
    public void onKick(List<User> userSelected) {
        for (User user : userSelected) {
            try {
                apiRequester.kickUser(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        finish();
    }
}
