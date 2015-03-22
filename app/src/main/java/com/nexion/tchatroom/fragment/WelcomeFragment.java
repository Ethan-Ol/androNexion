package com.nexion.tchatroom.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nexion.tchatroom.R;
import com.nexion.tchatroom.event.OnRoomAvailableEvent;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by DarzuL on 15/03/2015.
 */
public class WelcomeFragment extends Fragment {
    public static final String TAG = "NoChatRoomFragment";

    @InjectView(R.id.infoTv)
    TextView mInfoTv;
    @InjectView(R.id.connectBtn)
    Button mConnectBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);
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
        // TODO check if room is available
    }

    @Subscribe
    public void onRoomAvailable(OnRoomAvailableEvent event) {
        mInfoTv.setVisibility(View.GONE);
        mConnectBtn.setVisibility(View.VISIBLE);
    }
}
