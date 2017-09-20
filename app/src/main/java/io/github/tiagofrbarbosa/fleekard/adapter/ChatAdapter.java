package io.github.tiagofrbarbosa.fleekard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.model.Chat;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatsViewHolder>{

    private List<Chat> chats;
    private Context context;
    private final ChatAdapter.ChatOnclickListener onClickListener;

    @Inject
    Glide glide;

    public interface ChatOnclickListener{
        public void onClickChat(ChatAdapter.ChatsViewHolder holder, int idx);
    }

    public ChatAdapter(Context context, List<Chat> chats, ChatOnclickListener onClickListener){
        this.context = context;
        this.chats = chats;
        this.onClickListener = onClickListener;
    }

    @Override
    public ChatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_holder_chat, parent, false);
        ChatsViewHolder holder = new ChatsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ChatsViewHolder holder, final int position) {
        Chat chat = chats.get(position);
        glide.with(context).load(chat.img).apply(RequestOptions.circleCropTransform()).into(holder.img);
        holder.userName.setText(chat.userName);
        holder.userStatus.setText(chat.userStatus);

        if(chat.userPresence == 1){
            holder.userPresence.setImageResource(R.drawable.ic_connection_on);
        }else{
            holder.userPresence.setImageResource(R.drawable.ic_connection_off);
        }

        holder.chatUnread.setText(String.valueOf(chat.chatUnread));

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickChat(holder, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.chats != null ? this.chats.size() : 0;
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.user_image) ImageView img;
        @BindView(R.id.user_name) TextView userName;
        @BindView(R.id.user_status) TextView userStatus;
        @BindView(R.id.user_chat_presence) ImageView userPresence;
        @BindView(R.id.chat_unread) TextView chatUnread;
        private View view;

        public ChatsViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }
}
