package com.nexion.tchatroom.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.list.ChatAdapter;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.event.MessageReceivedEvent;
import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.Room;
import com.nexion.tchatroom.model.User;
import com.squareup.otto.Subscribe;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChatRoomFragment extends Fragment {
    private static final String ARG_ROOM = "room";
    public static final String TAG = "ChatRoomFragment";

    private Room mRoom;
    private OnFragmentInteractionListener mListener;

    @Inject
    User user;

    @InjectView(R.id.toolBar)
    Toolbar mToolbar;
    @InjectView(R.id.kickBtn)
    ImageButton mKickBtn;
    @InjectView(R.id.list)
    RecyclerView mRecyclerView;
    @InjectView(R.id.messageEt)
    EditText messageEt;

    private RecyclerView.LayoutManager mLayoutManager;
    private ChatAdapter mAdapter;

    public static ChatRoomFragment newInstance(Room room) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ROOM, room);
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
            mRoom = (Room) getArguments().getSerializable(ARG_ROOM);
        }

        mAdapter = new ChatAdapter(getActivity(), mRoom);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_room, container, false);
        ButterKnife.inject(this, v);

        if(!user.isAdmin()) {
            mKickBtn.setVisibility(View.GONE);
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        messageEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(messageEt.getWindowToken(), 0);

                        sendMessage();
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

    @OnClick(R.id.sendBtn)
    void sendMessage() {
        String content = messageEt.getText().toString();
        if(!content.isEmpty()) {
            messageEt.setText("");
            mListener.sendMessage(content);
            NexionMessage msg = createMessage(content);
            addMessage(msg);
        }
    }

    @OnClick(R.id.kickBtn)
    void openKickDialog() {
        mListener.showKickFragment();
    }

    @OnClick(R.id.leaveBtn)
    void onLeaveRoom() {
        mListener.leaveRoom();
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
        addMessage(event.getMessage());
    }

    private NexionMessage createMessage(String content) {
        NexionMessage msg = new NexionMessage();
        msg.setAuthor(user);
        msg.setContent(content);
        msg.setSendAt(null);

        return msg;
    }

    private void addMessage(NexionMessage msg) {
        mRoom.addMessage(msg);
        int messageCount = mRoom.countMessages();
        mAdapter.notifyItemInserted(mRoom.countMessages());
        mRecyclerView.scrollToPosition(messageCount - 1);
    }

    public interface OnFragmentInteractionListener {
        public void sendMessage(String content);
        public void leaveRoom();
        public void showKickFragment();
    }
}