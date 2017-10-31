package io.github.tiagofrbarbosa.fleekard.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.R;
import me.himanshusoni.chatmessageview.ChatMessageView;

/**
 * Created by tfbarbosa on 30/10/17.
 */

public class MessagesViewHolder extends RecyclerView.ViewHolder{
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

    public ImageView getPhotoImageView() {
        return this.photoImageView;
    }

    public ImageView getPhotoImageViewConnectedUser() {
        return this.photoImageViewConnectedUser;
    }

    public TextView getMessageTextView() {
        return this.messageTextView;
    }

    public TextView getAuthorTextView() {
        return this.authorTextView;
    }

    public TextView getMessageTextViewConnectedUser() {
        return this.messageTextViewConnectedUser;
    }

    public TextView getAuthorTextViewConnectedUser() {
        return this.authorTextViewConnectedUser;
    }

    public ChatMessageView getChatMessageViewUser() {
        return this.chatMessageViewUser;
    }

    public ChatMessageView getChatMessageViewUserConnected() {
        return this.chatMessageViewUserConnected;
    }

    public ChatMessageView getChatViewImage() {
        return this.chatViewImage;
    }

    public ChatMessageView getChatViewImageUserConnected() {
        return this.chatViewImageUserConnected;
    }
}
