package com.nexion.tchatroom.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nexion.tchatroom.R;
import com.nexion.tchatroom.model.ChatRoom;
import com.nexion.tchatroom.model.NexionMessage;
import com.nexion.tchatroom.model.User;

import java.text.DateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by DarzuL on 09/03/2015.
 * <p/>
 * A list with 4 different item which correspond
 * to the message from student, teacher, user and the bot
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private static final String TAG = "ChatAdapter";

    private final Context mContext;
    private final ChatRoom mChatRoom;

    public ChatAdapter(Context context, ChatRoom room) {
        mContext = context;
        ViewHolder.sAdapter = this;
        mChatRoom = room;
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

            case NexionMessage.MESSAGE_FROM_BOT:
                v = layoutInflater.inflate(R.layout.message_item_from_bot, viewGroup, false);
                return new ViewHolderBot(v);

            default:
                Log.e(TAG, "Message without type");
        }

        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        NexionMessage message = mChatRoom.getMessage(position);
        viewHolder.refreshView(message);
    }

    @Override
    public int getItemCount() {
        return mChatRoom.getMessages().size();
    }

    @Override
    public int getItemViewType(int position) {
        return mChatRoom.getMessage(position).getType();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private static ChatAdapter sAdapter;

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
            User author = sAdapter.mChatRoom.getUser(message.getAuthorId());
            pseudoTv.setText(author == null ? sAdapter.mContext.getString(R.string.text_unknown_user) : author.getPseudo());
            messageTv.setText(message.getContent());

            String dateText;
            if (message.getSendAt() == null) {
                dateText = sAdapter.mContext.getString(R.string.text_pending);
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

    static class ViewHolderBot extends ViewHolder {
        public ViewHolderBot(View itemView) {
            super(itemView);
        }

        @Override
        void refreshView(NexionMessage message) {
            messageTv.setText(message.getContent());
        }
    }
}