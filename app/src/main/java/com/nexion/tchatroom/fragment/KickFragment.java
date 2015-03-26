package com.nexion.tchatroom.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.list.KickAdapter;
import com.nexion.tchatroom.model.Room;
import com.nexion.tchatroom.model.User;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class KickFragment extends Fragment {

    public static final String TAG = "KickFragment";
    public static final String ARG_CURRENT_ROOM_ID = "current_room_id";

    private OnFragmentInteractionListener mListener;

    public static KickFragment newInstance(int currentRoomId) {
        KickFragment fragment = new KickFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_ROOM_ID, currentRoomId);
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    List<Room> rooms;

    @Inject
    User user;
    Room currentRoom;

    @InjectView(R.id.list)
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    KickAdapter mAdapter;


    public KickFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).inject(this);

        if(getArguments() != null) {
            int currentRoomId = getArguments().getInt(ARG_CURRENT_ROOM_ID);
            for(Room room : rooms) {
                if(room.getId() == currentRoomId) {
                    currentRoom = room;
                    break;
                }
            }
        }

        List<User> kickableUsers = new LinkedList<>();
        kickableUsers.addAll(currentRoom.getUsers());
        kickableUsers.remove(user);
        mAdapter = new KickAdapter(kickableUsers);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_kick, container, false);
        ButterKnife.inject(this, v);

        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        return v;
    }

    @OnClick(R.id.validBtn)
    void onValidKick() {
        mListener.onKick(mAdapter.userSelected);
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

    public interface OnFragmentInteractionListener {
        public void onKick(List<User> userSelected);
    }

}
