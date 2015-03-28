package com.nexion.tchatroom.list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final static String REG_URL = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private final static Pattern PATTERN = Pattern.compile(REG_URL, 0);

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
                return new ViewHolderUser(v, this);

            case NexionMessage.MESSAGE_FROM_TEACHER:
                v = layoutInflater.inflate(R.layout.message_item_from_teacher, viewGroup, false);
                return new ViewHolderTeacher(v, this);

            case NexionMessage.MESSAGE_FROM_STUDENT:
                v = layoutInflater.inflate(R.layout.message_item_from_student, viewGroup, false);
                return new ViewHolderStudent(v, this);

            case NexionMessage.MESSAGE_FROM_BOT:
                v = layoutInflater.inflate(R.layout.message_item_from_bot, viewGroup, false);
                return new ViewHolderBot(v, this);

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

    private void onOpenUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        static Context context;

        @InjectView(R.id.pseudoTv)
        TextView pseudoTv;
        @InjectView(R.id.messageTv)
        TextView messageTv;
        @InjectView(R.id.dateTv)
        TextView dateTv;

        ChatAdapter listener;

        public ViewHolder(View v, ChatAdapter listener) {
            super(v);
            ButterKnife.inject(this, v);

            this.listener = listener;
            v.setOnClickListener(this);
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

        @Override
        public void onClick(View v) {
            String text = messageTv.getText().toString();
            Matcher matcher = PATTERN.matcher(text);
            if (matcher.find()) {
                listener.onOpenUrl(matcher.group());
            }
        }
    }

    static class ViewHolderUser extends ViewHolder {

        public ViewHolderUser(View itemView, ChatAdapter listener) {
            super(itemView, listener);
        }
    }

    static class ViewHolderTeacher extends ViewHolder {

        public ViewHolderTeacher(View itemView, ChatAdapter listener) {
            super(itemView, listener);
        }
    }

    static class ViewHolderStudent extends ViewHolder {

        public ViewHolderStudent(View itemView, ChatAdapter listener) {
            super(itemView, listener);
        }
    }

    static class ViewHolderBot extends ViewHolder {
        public ViewHolderBot(View itemView, ChatAdapter listener) {
            super(itemView, listener);
        }

        @Override
        void refreshView(NexionMessage message) {
            messageTv.setText(message.getContent());
        }
    }
}