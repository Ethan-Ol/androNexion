package com.nexion.tchatroom.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class LoginFragment extends Fragment {
    public static final String TAG = "LoginFragment";
    private static final String ARG_USERNAME = "username";

    @InjectView(R.id.loaderLayout)
    LinearLayout mLoaderLayout;

    @InjectView(R.id.usernameEt)
    EditText mUsernameEt;

    @InjectView(R.id.passwordEt)
    EditText mPasswordEt;

    private String mUsername;

    private OnFragmentInteractionListener mListener;

    public static LoginFragment newInstance() {
        return new LoginFragment();
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

        mPasswordEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mPasswordEt.getWindowToken(), 0);

                        onLogin();
                        return true;
                    }
                }

                return false;
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.connectionBtn)
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

    public void onLoading() {
        mLoaderLayout.setVisibility(View.VISIBLE);
    }

    public void onEndLoading() {
        mLoaderLayout.setVisibility(View.GONE);
    }

    public interface OnFragmentInteractionListener {
        public void onLogin(String username, String password);
    }
}
