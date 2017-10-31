package io.github.tiagofrbarbosa.fleekard.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.R;

/**
 * Created by tfbarbosa on 30/10/17.
 */

public class ChatsViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.user_image) ImageView img;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.user_status) TextView userStatus;
    @BindView(R.id.delete_chat) ImageButton deleteChat;
    @BindView(R.id.user_chat_presence) ImageView userPresence;
    @BindView(R.id.chat_unread) TextView chatUnread;
    private View view;

    public ChatsViewHolder(View view) {
        super(view);
        this.view = view;
        ButterKnife.bind(this, view);
    }

    public ImageView getImg() {
        return this.img;
    }

    public TextView getUserName() {
        return this.userName;
    }

    public TextView getUserStatus() {
        return this.userStatus;
    }

    public ImageButton getDeleteChat() {
        return this.deleteChat;
    }

    public ImageView getUserPresence() {
        return this.userPresence;
    }

    public TextView getChatUnread() {
        return this.chatUnread;
    }
}
