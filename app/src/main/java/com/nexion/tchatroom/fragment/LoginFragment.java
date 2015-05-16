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
    private static final String ARG_PSEUDO = "pseudo";

    @InjectView(R.id.loaderLayout)
    LinearLayout mLoaderLayout;

    @InjectView(R.id.usernameEt)
    EditText mUsernameEt;

    @InjectView(R.id.passwordEt)
    EditText mPasswordEt;

    private OnFragmentInteractionListener mListener;

    public static LoginFragment newInstance(String pseudo) {
        LoginFragment fragment = new LoginFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PSEUDO, pseudo);
        fragment.setArguments(args);

        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this, v);

        Bundle args = getArguments();
        if(args != null) {
            String pseudo = args.getString(ARG_PSEUDO);
            mUsernameEt.setText(pseudo);
            mUsernameEt.setSelection(pseudo.length());
        }

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
        void onLogin(String username, String password);
    }
}
