package io.github.tiagofrbarbosa.fleekard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.holder.MessagesViewHolder;
import io.github.tiagofrbarbosa.fleekard.model.Message;
import io.github.tiagofrbarbosa.fleekard.utils.MyUtils;

/**
 * Created by tfbarbosa on 01/10/17.
 */

public class MessageChatAdapter extends RecyclerView.Adapter<MessagesViewHolder>{

    private List<Message> messages;
    private Context context;
    private final MessageOnclickListener onClickListener;
    private FleekardApplication app;
    private MyUtils mUtils;

    @Inject Glide glide;

    public interface MessageOnclickListener{
        void onClickUser(MessagesViewHolder holder, int idx);
    }

    public MessageChatAdapter(Context context, List<Message> messages, MessageOnclickListener onClickListener, FleekardApplication app){
        this.context = context;
        this.messages = messages;
        this.onClickListener = onClickListener;
        this.app = app;
        this.mUtils = new MyUtils(context);
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

            holder.getMessageTextView().setVisibility(View.GONE);
            holder.getAuthorTextView().setVisibility(View.GONE);
            holder.getPhotoImageView().setVisibility(View.GONE);
            holder.getChatMessageViewUser().setVisibility(View.GONE);
            holder.getChatViewImage().setVisibility(View.GONE);
            holder.getTimeImageView().setVisibility(View.GONE);

            holder.getMessageTextViewConnectedUser().setVisibility(View.VISIBLE);
            holder.getAuthorTextViewConnectedUser().setVisibility(View.VISIBLE);
            holder.getPhotoImageViewConnectedUser().setVisibility(View.VISIBLE);
            holder.getChatMessageViewUserConnected().setVisibility(View.VISIBLE);
            holder.getChatViewImageUserConnected().setVisibility(View.VISIBLE);
            holder.getTimeImageViewUserConnected().setVisibility(View.VISIBLE);

            if(isPhoto){
                holder.getMessageTextViewConnectedUser().setVisibility(View.GONE);
                holder.getAuthorTextViewConnectedUser().setVisibility(View.GONE);
                holder.getChatMessageViewUserConnected().setVisibility(View.GONE);
                holder.getChatViewImageUserConnected().setVisibility(View.VISIBLE);
                holder.getPhotoImageViewConnectedUser().setVisibility(View.VISIBLE);
                holder.getTimeImageViewUserConnected().setVisibility(View.VISIBLE);
                holder.getTimeImageViewUserConnected().setText(mTime);
                glide.with(context)
                        .load(message.getPhotoUrl())
                        .into(holder.getPhotoImageViewConnectedUser());
            }else{
                holder.getMessageTextViewConnectedUser().setVisibility(View.VISIBLE);
                holder.getAuthorTextViewConnectedUser().setVisibility(View.VISIBLE);
                holder.getChatMessageViewUserConnected().setVisibility(View.VISIBLE);
                holder.getAuthorTextViewConnectedUser().setText(mTime);
                holder.getChatViewImageUserConnected().setVisibility(View.GONE);
                holder.getPhotoImageViewConnectedUser().setVisibility(View.GONE);
                holder.getTimeImageViewUserConnected().setVisibility(View.GONE);
                holder.getMessageTextViewConnectedUser().setText(message.getText());
            }

        }else{

            holder.getMessageTextViewConnectedUser().setVisibility(View.GONE);
            holder.getAuthorTextViewConnectedUser().setVisibility(View.GONE);
            holder.getPhotoImageViewConnectedUser().setVisibility(View.GONE);
            holder.getChatMessageViewUserConnected().setVisibility(View.GONE);
            holder.getChatViewImageUserConnected().setVisibility(View.GONE);
            holder.getTimeImageViewUserConnected().setVisibility(View.GONE);

            holder.getMessageTextView().setVisibility(View.VISIBLE);
            holder.getAuthorTextView().setVisibility(View.VISIBLE);
            holder.getPhotoImageView().setVisibility(View.VISIBLE);
            holder.getChatMessageViewUser().setVisibility(View.VISIBLE);
            holder.getChatViewImage().setVisibility(View.VISIBLE);
            holder.getTimeImageView().setVisibility(View.VISIBLE);

            if(isPhoto){
                holder.getMessageTextView().setVisibility(View.GONE);
                holder.getAuthorTextView().setVisibility(View.GONE);
                holder.getChatMessageViewUser().setVisibility(View.GONE);
                holder.getChatViewImage().setVisibility(View.VISIBLE);
                holder.getTimeImageView().setVisibility(View.VISIBLE);
                holder.getPhotoImageView().setVisibility(View.VISIBLE);
                holder.getTimeImageView().setText(mTime);
                glide.with(context)
                            .load(message.getPhotoUrl())
                            .into(holder.getPhotoImageView());

            }else{
                holder.getMessageTextView().setVisibility(View.VISIBLE);
                holder.getAuthorTextView().setVisibility(View.VISIBLE);
                holder.getChatMessageViewUser().setVisibility(View.VISIBLE);
                holder.getAuthorTextView().setText(mTime);
                holder.getChatViewImage().setVisibility(View.GONE);
                holder.getPhotoImageView().setVisibility(View.GONE);
                holder.getTimeImageView().setVisibility(View.GONE);
                holder.getMessageTextView().setText(message.getText());
            }
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
}