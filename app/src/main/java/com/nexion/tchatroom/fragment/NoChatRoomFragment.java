package com.nexion.tchatroom.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nexion.tchatroom.R;

/**
 * Created by DarzuL on 15/03/2015.
 */
public class NoChatRoomFragment extends Fragment {
    public static final String TAG = "NoChatRoomFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_no_chat_room, container, false);
        return v;
    }
}
