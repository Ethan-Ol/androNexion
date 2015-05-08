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

import com.nexion.tchatroom.App;
import com.nexion.tchatroom.BeaconOrganizer;
import com.nexion.tchatroom.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by DarzuL on 15/03/2015.
 * <p/>
 * Waiting mRoom fragment when mUser is not in a mRoom
 */
public class WaitingRoomFragment extends Fragment implements BeaconOrganizer.BeaconOrganizerListener {
    public static final String TAG = "NoChatRoomFragment";

    @InjectView(R.id.loaderLayout)
    LinearLayout mLoaderLayout;
    @InjectView(R.id.infoTv)
    TextView mInfoTv;
    @InjectView(R.id.connectBtn)
    Button mConnectBtn;

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

        //TODO debug mode
        mInfoTv.setVisibility(View.VISIBLE);
        //mConnectBtn.setVisibility(View.VISIBLE);

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

        Integer roomId;
        if ((roomId = BeaconOrganizer.attachListener(this)) != null) {
            mListener.onRoomAvailable(roomId);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BeaconOrganizer.detachListener(this);
    }

    public void onRoomAvailable(int roomId) {
        mInfoTv.setVisibility(View.GONE);
        mConnectBtn.setVisibility(View.VISIBLE);
        mListener.onRoomAvailable(roomId);
    }

    public void onRoomUnavailable() {
        mConnectBtn.setVisibility(View.GONE);
        mInfoTv.setVisibility(View.VISIBLE);
        mListener.onRoomUnavailable();
    }

    public void onLoading() {
        mInfoTv.setVisibility(View.GONE);
        mConnectBtn.setVisibility(View.GONE);
        mLoaderLayout.setVisibility(View.VISIBLE);
    }

    public void onEndLoading() {
        mLoaderLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.connectBtn)
    void onJoinRoom() {
        mListener.onJoinRoom();
    }


    public interface OnFragmentInteractionListener {
        void onJoinRoom();

        void onRoomAvailable(int roomId);

        void onRoomUnavailable();
    }
}
