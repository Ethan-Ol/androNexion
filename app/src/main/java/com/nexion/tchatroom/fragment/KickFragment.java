package com.nexion.tchatroom.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nexion.tchatroom.R;
import com.nexion.tchatroom.list.KickAdapter;
import com.nexion.tchatroom.model.ChatRoom;
import com.nexion.tchatroom.model.User;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class KickFragment extends Fragment implements KickAdapter.KickFragmentListener {

    public static final String TAG = "KickFragment";

    private OnFragmentInteractionListener mListener;

    public static KickFragment newInstance() {
        return new KickFragment();
    }

    private final List<User> users = new LinkedList<>();
    private final List<User> userSelected = new LinkedList<>();

    @InjectView(R.id.validBtn)
    Button mValidButton;
    @InjectView(R.id.list)
    RecyclerView mRecyclerView;

    private KickAdapter mAdapter;


    public KickFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChatRoom chatRoom = mListener.fragmentCreated();
        for (User user : chatRoom.getUsers().values()) {
            if (user.isInRoom() && user.getId() != User.currentUserId)
                users.add(user);
        }

        mAdapter = new KickAdapter(this, users, userSelected);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_kick, container, false);
        ButterKnife.inject(this, v);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        return v;
    }

    @OnClick(R.id.validBtn)
    void onValidKick() {
        mListener.onKick(userSelected);

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

    @Override
    public void onItemClicked() {
        switch (userSelected.size()) {
            case 0:
                mValidButton.setText(getString(R.string.text_cancel));
                break;

            default:
                List<String> pseudoList = new LinkedList<>();
                for (User user : userSelected) {
                    pseudoList.add(user.getPseudo());
                }
                String pseudoListJoined = TextUtils.join(", ", pseudoList);
                mValidButton.setText(getString(R.string.text_kick_user, pseudoListJoined));
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        ChatRoom fragmentCreated();

        void onKick(List<User> userSelected);
    }

}
