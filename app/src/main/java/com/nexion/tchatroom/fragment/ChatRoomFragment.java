package com.nexion.tchatroom.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.ChatAdapter;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.event.MessageReceivedEvent;
import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.Room;
import com.squareup.otto.Subscribe;

import org.parceler.Parcel;
import org.parceler.Parcels;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChatRoomFragment extends Fragment {
    private static final String ARG_ROOM = "room";

    private Room mRoom;
    private OnFragmentInteractionListener mListener;

    @InjectView(R.id.list)
    RecyclerView mRecyclerView;
    @InjectView(R.id.messageEt)
    EditText messageEt;
    private ChatAdapter mAdapter;

    public static ChatRoomFragment newInstance(Room room) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ROOM, Parcels.wrap(room));
        fragment.setArguments(args);
        return fragment;
    }

    public ChatRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).inject(this);

        if (getArguments() != null) {
            mRoom = Parcels.unwrap(getArguments().getParcelable(ARG_ROOM));
        }

        mAdapter = new ChatAdapter(mRoom);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_room, container, false);
        ButterKnife.inject(this, v);

        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.sendBtn)
    void sendMessage() {
        String content = messageEt.getText().toString();
        if(!content.isEmpty())
            mListener.sendMessage(content);
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
    void onMessageReceive(MessageReceivedEvent event) {
        mRoom.addMessage(event.getMessage());
        mAdapter.notifyItemInserted(mRoom.countMessages());
    }

    public interface OnFragmentInteractionListener {
        public void sendMessage(String content);
        public void leaveRoom();
    }

}
