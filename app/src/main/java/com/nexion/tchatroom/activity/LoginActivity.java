package com.nexion.tchatroom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nexion.tchatroom.App;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.api.APIRequester;
import com.nexion.tchatroom.api.ErrorHandler;
import com.nexion.tchatroom.fragment.LoginFragment;
import com.nexion.tchatroom.manager.KeyFields;
import com.nexion.tchatroom.manager.PlayServicesManager;
import com.nexion.tchatroom.model.User;
import com.squareup.otto.Bus;

import org.json.JSONException;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements LoginFragment.OnFragmentInteractionListener, APIRequester.UserInfoListener, KeyFields, PlayServicesManager.PlayServicesListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final static String LOGIN_FRAGMENT_TAG = "LoginFragment";

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Inject
    Bus bus;

    private APIRequester apiRequester;
    private String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ((App) getApplication()).inject(this);

        apiRequester = new APIRequester(getApplicationContext());
        if (checkPlayServices()) {
            PlayServicesManager psm = new PlayServicesManager(getApplicationContext());
            regid = psm.getRegistrationId(this);

            if(regid.isEmpty()) {
                showLoading();
                psm.registerInBackground(this);
            }
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(LOGIN_FRAGMENT_TAG);
        if (fragment == null) {
            if (getSharedPreferences(KeyFields.PREF_FILE, Context.MODE_PRIVATE).contains(KeyFields.KEY_TOKEN)) {
                startWaitingRoom();
            } else {
                String userLogin = getSharedPreferences(KeyFields.PREF_FILE, Context.MODE_PRIVATE).getString(KeyFields.KEY_USER_PSEUDO, "");
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, LoginFragment.newInstance(userLogin), LOGIN_FRAGMENT_TAG)
                        .commit();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override

    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    public void onLogin(String username, String password) {
        try {
            apiRequester.requestToken(username, password, regid, LoginActivity.this);
            showLoading();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startWaitingRoom() {
        startActivity(new Intent(getApplicationContext(), WaitingRoomActivity.class));
        finish();
    }

    @Override
    public void onUserConnected(String token, User user) {
        getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_TOKEN, token)
                .putInt(KEY_USER_ID, user.getId())
                .putString(KEY_USER_PSEUDO, user.getPseudo())
                .putInt(KEY_USER_ACL, user.getAcl())
                .apply();

        User.currentUserId = user.getId();

        startWaitingRoom();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ErrorHandler.toastError(this, error);
        hideLoading();
    }

    private LoginFragment getLoginFragment() {
        return (LoginFragment) getSupportFragmentManager().findFragmentByTag(LOGIN_FRAGMENT_TAG);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void showLoading() {
        LoginFragment fragment = getLoginFragment();
        if (fragment != null) {
            fragment.onLoading();
        }
    }

    private void hideLoading() {
        LoginFragment fragment = getLoginFragment();
        if (fragment != null) {
            fragment.onEndLoading();
        }
    }

    @Override
    public void onRegistrationFinish(String newRegId) {
        regid = newRegId;
        hideLoading();
    }
}
