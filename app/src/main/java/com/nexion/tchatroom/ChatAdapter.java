package com.nexion.tchatroom;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.Room;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by DarzuL on 09/03/2015.
 *
 * A list with 3 different item which correspond
 * to the message from student, teacher and the user
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private static final String TAG = "ChatAdapter";

    private final Room room;

    public ChatAdapter(Room room) {
        this.room = room;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        View v;
        switch (viewType) {
            case NexionMessage.MESSAGE_FROM_USER:
                v = layoutInflater.inflate(R.layout.message_item_from_user, viewGroup);
                return new ViewHolderUser(v);

            case NexionMessage.MESSAGE_FROM_TEACHER:
                v = layoutInflater.inflate(R.layout.message_item_from_teacher, viewGroup);
                return new ViewHolderTeacher(v);

            case NexionMessage.MESSAGE_FROM_STUDENT:
                v = layoutInflater.inflate(R.layout.message_item_from_student, viewGroup);
                return new ViewHolderStudent(v);

            default:
                Log.e(TAG, "Message without type");
        }


        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        NexionMessage message = room.getMessages().get(position);
        viewHolder.refreshView(message);
    }

    @Override
    public int getItemCount() {
        return room.countMessages();
    }

    @Override
    public int getItemViewType(int position) {
        return room.getMessages().get(position).getType();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.messageTv)
        TextView messageTv;

        public ViewHolder(View v) {
            super(v);

            ButterKnife.inject(this, v);
        }

        void refreshView(NexionMessage message) {
            messageTv.setText(message.getContent());
        }
    }

    static class ViewHolderUser extends ViewHolder {

        public ViewHolderUser(View itemView) {
            super(itemView);
        }
    }

    static class ViewHolderTeacher extends ViewHolder {

        public ViewHolderTeacher(View itemView) {
            super(itemView);
        }
    }

    static class ViewHolderStudent extends ViewHolder {

        public ViewHolderStudent(View itemView) {
            super(itemView);
        }
    }
}