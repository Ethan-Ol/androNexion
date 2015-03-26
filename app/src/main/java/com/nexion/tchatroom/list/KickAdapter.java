package com.nexion.tchatroom.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nexion.tchatroom.R;
import com.nexion.tchatroom.model.Room;
import com.nexion.tchatroom.model.User;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by DarzuL on 25/03/2015.
 *
 * Adapter to manage user to kick them
 */
public class KickAdapter extends RecyclerView.Adapter<KickAdapter.ViewHolder> {

    public List<User> users;
    public List<User> userSelected;

    public KickAdapter(List<User> users) {
        this.users = users;
        userSelected = new LinkedList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false), this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);
        boolean selected = userSelected.contains(user);
        holder.refresh(user, selected);
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    public boolean onUserItemClick(User user) {
        if(userSelected.contains(user)) {
            userSelected.remove(user);
            return false;
        }
        else {
            userSelected.add(user);
            return true;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @InjectView(R.id.pseudoTv)
        TextView pseudoTv;
        KickAdapter listener;

        User mUser;

        public ViewHolder(View itemView, KickAdapter listener) {
            super(itemView);
            ButterKnife.inject(this, itemView);

            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        public void refresh(User user, boolean selected) {
            mUser = user;
            pseudoTv.setText(user.getPseudo());
            if(selected) {
                itemView.setBackgroundResource(R.color.item_selected);
            }
        }

        @Override
        public void onClick(View v) {
            if(listener.onUserItemClick(mUser)) {
                v.setBackgroundResource(R.color.item_selected);
            }
            else {
                v.setBackgroundResource(R.color.item_not_selected);
            }
        }
    }
}