package com.nexion.tchatroom.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nexion.tchatroom.R;
import com.nexion.tchatroom.model.NexionMessage;
import com.squareup.otto.Subscribe;

public class ChatRoomFragment extends Fragment {
    private static final String ARG_ROOM = "room";

    private String mRoom;

    private OnFragmentInteractionListener mListener;

    public static ChatRoomFragment newInstance(String param1, String param2) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROOM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ChatRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRoom = getArguments().getString(ARG_ROOM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_room, container, false);

        return v;
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
    void onMessageReceive(NexionMessage msg) {

    }

    public interface OnFragmentInteractionListener {
        public void sendMessage(Uri uri);
    }

}
