package com.nexion.tchatroom.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.event.EndLoadingEvent;
import com.nexion.tchatroom.event.LoadingEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class LoginFragment extends Fragment {
    public static final String TAG = "LoginFragment";
    private static final String ARG_USERNAME = "username";

    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;

    @InjectView(R.id.usernameEt)
    EditText mUsernameEt;

    @InjectView(R.id.passwordEt)
    EditText mPasswordEt;

    private String mUsername;

    private OnFragmentInteractionListener mListener;

    @Inject
    Bus bus;

    public static LoginFragment newInstance(String username) {
        LoginFragment fragment = new LoginFragment();

        if (username != null) {
            Bundle args = new Bundle();
            args.putString(ARG_USERNAME, username);
            fragment.setArguments(args);
        }

        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).inject(this);

        if (getArguments() != null) {
            mUsername = getArguments().getString(ARG_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this, v);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @OnClick(R.id.connexionBtn)
    public void onLogin() {
        if (mListener != null) {
            String username = mUsernameEt.getText().toString();
            String password = mPasswordEt.getText().toString();

            if (username.isEmpty()) {
                Toast.makeText(getActivity(), getResources().getString(R.string.text_no_username), Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(getActivity(), getResources().getString(R.string.text_no_password), Toast.LENGTH_SHORT).show();
            } else {
                mListener.onLogin(username, password);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Subscribe
    public void onLoading(LoadingEvent event) {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onEndLoading(EndLoadingEvent event) {
        mProgressBar.setVisibility(View.GONE);
    }

    public interface OnFragmentInteractionListener {
        public void onLogin(String username, String password);
    }

}
