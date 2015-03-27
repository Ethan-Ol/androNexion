package com.nexion.tchatroom.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nexion.tchatroom.R;
import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.Room;

import java.text.DateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by DarzuL on 09/03/2015.
 * <p/>
 * A list with 3 different item which correspond
 * to the message from student, teacher and the user
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private static final String TAG = "ChatAdapter";

    private final Room room;
    private final Context context;

    public ChatAdapter(Context context, Room room) {
        this.context = context;
        ViewHolder.context = context;
        this.room = room;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        View v;
        switch (viewType) {
            case NexionMessage.MESSAGE_FROM_USER:
                v = layoutInflater.inflate(R.layout.message_item_from_user, viewGroup, false);
                return new ViewHolderUser(v);

            case NexionMessage.MESSAGE_FROM_TEACHER:
                v = layoutInflater.inflate(R.layout.message_item_from_teacher, viewGroup, false);
                return new ViewHolderTeacher(v);

            case NexionMessage.MESSAGE_FROM_STUDENT:
                v = layoutInflater.inflate(R.layout.message_item_from_student, viewGroup, false);
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

        static Context context;

        @InjectView(R.id.pseudoTv)
        TextView pseudoTv;
        @InjectView(R.id.messageTv)
        TextView messageTv;
        @InjectView(R.id.dateTv)
        TextView dateTv;

        public ViewHolder(View v) {
            super(v);

            ButterKnife.inject(this, v);
        }

        void refreshView(NexionMessage message) {
            pseudoTv.setText(message.getAuthor().getPseudo());
            messageTv.setText(message.getContent());

            String dateText;
            if (message.getSendAt() == null) {
                dateText = context.getString(R.string.pending);
            } else {
                dateText = DateFormat.getTimeInstance(DateFormat.SHORT).format(message.getSendAt().getTime());
            }
            dateTv.setText(dateText);
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