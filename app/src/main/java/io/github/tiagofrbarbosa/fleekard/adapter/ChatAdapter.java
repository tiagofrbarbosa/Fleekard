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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.model.Chat;
import io.github.tiagofrbarbosa.fleekard.model.User;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatsViewHolder>{

    private Context context;
    private List<Chat> chats;
    private final ChatAdapter.ChatOnclickListener onClickListener;
    private DatabaseReference mUserReference;

    @Inject
    Glide glide;

    public interface ChatOnclickListener{
        public void onClickChat(ChatAdapter.ChatsViewHolder holder, int idx);
    }

    public ChatAdapter(Context context, List<Chat> chats, ChatOnclickListener onClickListener,
                       DatabaseReference mUserReference){

        this.context = context;
        this.chats = chats;
        this.onClickListener = onClickListener;
        this.mUserReference = mUserReference;
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

            mUserReference
                    .orderByChild(Database.users.USER_KEY)
                    .equalTo(chat.getUserId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                                User user = userSnap.getValue(User.class);
                                glide.with(context).load(user.getImg()).apply(RequestOptions.circleCropTransform()).into(holder.img);
                                holder.userName.setText(user.getUserName());
                                holder.userStatus.setText(user.getUserStatus());

                                if (user.getUserPresence() == 1) {
                                    holder.userPresence.setImageResource(android.R.drawable.presence_online);
                                } else {
                                    holder.userPresence.setImageResource(android.R.drawable.presence_offline);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            holder.chatUnread.setText(String.valueOf(chat.getMsgUnread()));

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
