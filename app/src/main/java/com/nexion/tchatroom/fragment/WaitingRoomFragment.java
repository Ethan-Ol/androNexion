package com.nexion.tchatroom.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.R;
import com.nexion.tchatroom.event.EndLoadingEvent;
import com.nexion.tchatroom.event.LoadingEvent;
import com.nexion.tchatroom.event.OnRoomAvailableEvent;
import com.nexion.tchatroom.event.RequestFailedEvent;
import com.nexion.tchatroom.model.User;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by DarzuL on 15/03/2015.
 *
 * Waiting room fragment when user is not in a room
 */
public class WaitingRoomFragment extends Fragment {
    public static final String TAG = "NoChatRoomFragment";

    @InjectView(R.id.loaderLayout)
    LinearLayout mLoaderLayout;
    @InjectView(R.id.infoTv)
    TextView mInfoTv;
    @InjectView(R.id.connectBtn)
    Button mConnectBtn;

    @Inject
    Bus bus;
    @Inject
    User user;

    private OnFragmentInteractionListener mListener;

    public static Fragment newInstance() {
        return new WaitingRoomFragment();
    }

    public WaitingRoomFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_waiting_room, container, false);
        ButterKnife.inject(this, v);

        //TODO Test
        //mInfoTv.setVisibility(View.VISIBLE);
        mConnectBtn.setVisibility(View.VISIBLE);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
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
    public void onStart() {
        super.onStart();
        bus.register(this);
        // TODO check if room is available
    }

    @Override
    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Subscribe
    public void onRoomAvailable(OnRoomAvailableEvent event) {
        mInfoTv.setVisibility(View.GONE);
        mConnectBtn.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onLoading(LoadingEvent event) {
        mInfoTv.setVisibility(View.GONE);
        mConnectBtn.setVisibility(View.GONE);
        mLoaderLayout.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onEndLoading(EndLoadingEvent event) {
        mLoaderLayout.setVisibility(View.GONE);
    }

    @Subscribe
    public void onRequestFailedEvent(RequestFailedEvent event) {
        Toast.makeText(getActivity(), event.toString(), Toast.LENGTH_SHORT).show();
        mLoaderLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.connectBtn)
    void onJoinRoom() {
        mListener.onJoinRoom();
    }


    public interface OnFragmentInteractionListener {
        public void onJoinRoom();
    }
}
