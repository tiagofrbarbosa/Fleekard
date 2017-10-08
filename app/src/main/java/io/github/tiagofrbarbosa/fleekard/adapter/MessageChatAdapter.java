package io.github.tiagofrbarbosa.fleekard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.model.Message;
import io.github.tiagofrbarbosa.fleekard.model.User;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 01/10/17.
 */

public class MessageChatAdapter extends RecyclerView.Adapter<MessageChatAdapter.MessagesViewHolder>{

    private List<Message> messages;
    private Context context;
    private final MessageOnclickListener onClickListener;

    @Inject Glide glide;

    public interface MessageOnclickListener{
        public void onClickUser(MessagesViewHolder holder, int idx);
    }

    public MessageChatAdapter(Context context, List<Message> messages, MessageOnclickListener onClickListener){
        this.context = context;
        this.messages = messages;
        this.onClickListener = onClickListener;
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.textview_holder_message, parent, false);
        MessagesViewHolder holder = new MessagesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MessagesViewHolder holder, final int position) {
        Message message = messages.get(position);

        boolean isPhoto = message.getPhotoUrl() != null;

        if(isPhoto){
            holder.messageTextView.setVisibility(View.GONE);
            holder.photoImageView.setVisibility(View.VISIBLE);
            glide.with(context)
                    .load(message.getPhotoUrl())
                    .into(holder.photoImageView);
        }else{
            holder.messageTextView.setVisibility(View.VISIBLE);
            holder.photoImageView.setVisibility(View.GONE);
            holder.messageTextView.setText(message.getText());
        }

        long mSystemTime = message.getTimeStampLong();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(mSystemTime);
        String mTime = simpleDateFormat.format(date);

        holder.authorTextView.setText(mTime + " " + message.getName());

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickUser(holder, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.messages != null ? this.messages.size() : 0;
    }

    public static class MessagesViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.photoImageView) ImageView photoImageView;
        @BindView(R.id.messageTextView) TextView messageTextView;
        @BindView(R.id.nameTextView) TextView authorTextView;

        private View view;

        public MessagesViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }
}