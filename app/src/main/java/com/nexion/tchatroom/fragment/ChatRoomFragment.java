package com.nexion.tchatroom.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.event.JoinReceivedEvent;
import com.nexion.tchatroom.event.KickReceivedEvent;
import com.nexion.tchatroom.event.LeaveReceivedEvent;
import com.nexion.tchatroom.event.MessageReceivedEvent;
import com.nexion.tchatroom.list.ChatAdapter;
import com.nexion.tchatroom.manager.KeyFields;
import com.nexion.tchatroom.model.ChatRoom;
import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.User;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChatRoomFragment extends Fragment implements KeyFields {
    private static final String ARG_ROOM = "mRoom";
    public static final String TAG = "ChatRoomFragment";

    private User mUser;
    private ChatRoom mRoom;
    private OnFragmentInteractionListener mListener;

    @InjectView(R.id.toolBar)
    Toolbar mToolbar;
    @InjectView(R.id.title)
    TextView titleTv;
    @InjectView(R.id.kickBtn)
    ImageButton mKickBtn;
    @InjectView(R.id.list)
    RecyclerView mRecyclerView;
    @InjectView(R.id.messageEt)
    EditText messageEt;

    @Inject
    Bus bus;

    private RecyclerView.LayoutManager mLayoutManager;
    private ChatAdapter mAdapter;
    private Map<Integer, User> mUserMap;
    private List<NexionMessage> mMessages;

    public static ChatRoomFragment newInstance() {
        return new ChatRoomFragment();
    }

    public ChatRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).inject(this);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        int userId = sharedPref.getInt(KEY_USER_ID, 0);
        String userPseudo = sharedPref.getString(KEY_USER_PSEUDO, "");
        int userAcl = sharedPref.getInt(KEY_USER_ACL, 0);
        mUser = new User(userId, userPseudo, userAcl);

        mUserMap = new HashMap<>();
        mUserMap.put(mUser.getId(), mUser);

        mAdapter = new ChatAdapter(getActivity(), mRoom);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_room, container, false);
        ButterKnife.inject(this, v);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

        titleTv.setText(mRoom.getName());

        messageEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
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
    public void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.sendBtn)
    void sendMessage() {
        String content = messageEt.getText().toString();
        if (!content.isEmpty()) {
            messageEt.setText("");
            mListener.sendMessage(content);
            NexionMessage msg = createOwnMessage(content);
            addMessage(msg);
        }
    }

    @OnClick(R.id.kickBtn)
    void openKickDialog() {
        mListener.startKickActivity();
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

    private NexionMessage createOwnMessage(String content) {
        return new NexionMessage(content, null, mUser.getId(), NexionMessage.MESSAGE_FROM_USER);
    }

    private void addMessage(NexionMessage msg) {
        mRoom.addMessage(msg);
        int messageCount = mRoom.countMessages();
        mAdapter.notifyItemInserted(mRoom.countMessages());
        mRecyclerView.scrollToPosition(messageCount - 1);
    }

    private void unPendingMsg() {
        List<NexionMessage> messages = mRoom.getMessages();
        int len = messages.size();

        for (int i = len - 1; i >= 0; i--) {
            NexionMessage message = messages.get(i);
            if (message.getSendAt() == null && message.getType() == NexionMessage.MESSAGE_FROM_USER) {
                message.unpending(Calendar.getInstance());
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe
    public void onMessageReceive(MessageReceivedEvent event) {
        User author = mUserMap.get(event.getAuthorId());

        int type = author.getId() == mUser.getId() ?
                NexionMessage.MESSAGE_FROM_USER :
                author.isAdmin() ?
                        NexionMessage.MESSAGE_FROM_TEACHER :
                        NexionMessage.MESSAGE_FROM_STUDENT;

        NexionMessage message = new NexionMessage(event.getContent(), event.getDateTime(), author.getId(), type);

        if (type == NexionMessage.MESSAGE_FROM_USER) {
            unPendingMsg();
        } else {
            addMessage(message);
        }
    }

    @Subscribe
    public void onUserJoined(JoinReceivedEvent event) {
        User user = event.getUser();
        mRoom.addUser(user);
        NexionMessage msg = new NexionMessage(getString(R.string.has_joined_room, user.getPseudo()), null, null, NexionMessage.MESSAGE_FROM_BOT);
        addMessage(msg);
    }

    @Subscribe
    public void onUserLeft(LeaveReceivedEvent event) {
        String pseudo = mUserMap.get(event.getUserId()).getPseudo();
        mUserMap.remove(event.getUserId());
        NexionMessage msg = new NexionMessage(getString(R.string.has_left_room, pseudo), null, null, NexionMessage.MESSAGE_FROM_BOT);
        addMessage(msg);
    }

    @Subscribe
    public void onUserKicked(KickReceivedEvent event) {
        int userId = event.getUserId();
        String pseudo = mUserMap.get(userId).getPseudo();
        mUserMap.remove(userId);

        NexionMessage msg = new NexionMessage(getString(R.string.has_kicked, pseudo), null, null, NexionMessage.MESSAGE_FROM_BOT);
        addMessage(msg);

        if (userId == mUser.getId()) {
            mListener.leaveRoom();
        }
    }

    public interface OnFragmentInteractionListener {
        public void sendMessage(String content);

        public void leaveRoom();

        public void startKickActivity();
    }
}