package io.github.tiagofrbarbosa.fleekard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.model.Message;
import io.github.tiagofrbarbosa.fleekard.utils.myUtils;
import me.himanshusoni.chatmessageview.ChatMessageView;

/**
 * Created by tfbarbosa on 01/10/17.
 */

public class MessageChatAdapter extends RecyclerView.Adapter<MessageChatAdapter.MessagesViewHolder>{

    private List<Message> messages;
    private Context context;
    private final MessageOnclickListener onClickListener;
    private FleekardApplication app;
    private myUtils mUtils;

    @Inject Glide glide;

    public interface MessageOnclickListener{
        public void onClickUser(MessagesViewHolder holder, int idx);
    }

    public MessageChatAdapter(Context context, List<Message> messages, MessageOnclickListener onClickListener, FleekardApplication app){
        this.context = context;
        this.messages = messages;
        this.onClickListener = onClickListener;
        this.app = app;
        this.mUtils = new myUtils(context);
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

        String mTime = mUtils.longToDate(message.getTimeStampLong(), "HH:mm");

        if(message.getUserId().equals(app.getmAppUser().getUserId())){

            holder.messageTextView.setVisibility(View.GONE);
            holder.authorTextView.setVisibility(View.GONE);
            holder.photoImageView.setVisibility(View.GONE);
            holder.chatMessageViewUser.setVisibility(View.GONE);
            holder.chatViewImage.setVisibility(View.GONE);

            holder.messageTextViewConnectedUser.setVisibility(View.VISIBLE);
            holder.authorTextViewConnectedUser.setVisibility(View.VISIBLE);
            holder.photoImageViewConnectedUser.setVisibility(View.VISIBLE);
            holder.chatMessageViewUserConnected.setVisibility(View.VISIBLE);
            holder.chatViewImageUserConnected.setVisibility(View.VISIBLE);

            if(isPhoto){
                holder.messageTextViewConnectedUser.setVisibility(View.GONE);
                holder.chatViewImageUserConnected.setVisibility(View.VISIBLE);
                holder.photoImageViewConnectedUser.setVisibility(View.VISIBLE);
                glide.with(context)
                        .load(message.getPhotoUrl())
                        .into(holder.photoImageViewConnectedUser);
            }else{
                holder.messageTextViewConnectedUser.setVisibility(View.VISIBLE);
                holder.chatViewImageUserConnected.setVisibility(View.GONE);
                holder.photoImageViewConnectedUser.setVisibility(View.GONE);
                holder.messageTextViewConnectedUser.setText(message.getText());
            }
            holder.authorTextViewConnectedUser.setText(mTime);

        }else{

            holder.messageTextViewConnectedUser.setVisibility(View.GONE);
            holder.authorTextViewConnectedUser.setVisibility(View.GONE);
            holder.photoImageViewConnectedUser.setVisibility(View.GONE);
            holder.chatMessageViewUserConnected.setVisibility(View.GONE);
            holder.chatViewImageUserConnected.setVisibility(View.GONE);

            holder.messageTextView.setVisibility(View.VISIBLE);
            holder.authorTextView.setVisibility(View.VISIBLE);
            holder.photoImageView.setVisibility(View.VISIBLE);
            holder.chatMessageViewUser.setVisibility(View.VISIBLE);
            holder.chatViewImage.setVisibility(View.VISIBLE);

            if(isPhoto){
                holder.messageTextView.setVisibility(View.GONE);
                holder.chatViewImage.setVisibility(View.VISIBLE);
                holder.photoImageView.setVisibility(View.VISIBLE);
                glide.with(context)
                        .load(message.getPhotoUrl())
                        .into(holder.photoImageView);
            }else{
                holder.messageTextView.setVisibility(View.VISIBLE);
                holder.chatViewImage.setVisibility(View.GONE);
                holder.photoImageView.setVisibility(View.GONE);
                holder.messageTextView.setText(message.getText());
            }
            holder.authorTextView.setText(mTime);

        }

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
        @BindView(R.id.photoImageViewConnectedUser) ImageView photoImageViewConnectedUser;
        @BindView(R.id.messageTextView) TextView messageTextView;
        @BindView(R.id.nameTextView) TextView authorTextView;
        @BindView(R.id.messageTextViewConnectedUser) TextView messageTextViewConnectedUser;
        @BindView(R.id.nameTextViewConnectedUser) TextView authorTextViewConnectedUser;
        @BindView(R.id.chatViewUser) ChatMessageView chatMessageViewUser;
        @BindView(R.id.chatViewUserConnected) ChatMessageView chatMessageViewUserConnected;
        @BindView(R.id.chatViewImage) ChatMessageView chatViewImage;
        @BindView(R.id.chatViewImageUserConnected) ChatMessageView chatViewImageUserConnected;

        private View view;

        public MessagesViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }
}