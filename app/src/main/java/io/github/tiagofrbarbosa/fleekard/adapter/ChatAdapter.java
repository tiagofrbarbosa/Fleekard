package io.github.tiagofrbarbosa.fleekard.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.holder.ChatsViewHolder;
import io.github.tiagofrbarbosa.fleekard.model.Chat;
import io.github.tiagofrbarbosa.fleekard.model.Message;
import io.github.tiagofrbarbosa.fleekard.model.User;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatsViewHolder>{

    private Context context;
    private List<Chat> chats;
    private List<Message> messages;
    private final ChatAdapter.ChatOnclickListener onClickListener;
    private FleekardApplication app;
    private DatabaseReference mUserReference;
    private DatabaseReference mMessageReference;

    @Inject
    Glide glide;

    public interface ChatOnclickListener{
        void onClickChat(ChatsViewHolder holder, int idx);
    }

    public ChatAdapter(Context context, List<Chat> chats, ChatOnclickListener onClickListener,
                       FleekardApplication app){

        this.context = context;
        this.chats = chats;
        this.onClickListener = onClickListener;
        this.app = app;
        this.mUserReference = app.getmFirebaseDatabase().getReference().child(Database.users.CHILD_USERS);
        this.mMessageReference = app.getmFirebaseDatabase().getReference().child(Database.messages.CHILD_MESSAGES);
        this.messages = new ArrayList<Message>();
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

        holder.getChatUnread().setVisibility(View.INVISIBLE);

            mUserReference
                    .orderByChild(Database.users.USER_KEY)
                    .equalTo(chat.getUserId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                                final User user = userSnap.getValue(User.class);

                                if(!user.getImg().equals(Database.users.USER_IMAGE_AVATAR)) {
                                    try {
                                        glide.with(context).load(user.getImg())
                                                .apply(RequestOptions.circleCropTransform()).into(holder.getImg());
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                }else{
                                    try {
                                        glide.with(context)
                                                .load(Database.users.USER_AVATAR_IMG)
                                                .apply(RequestOptions.circleCropTransform())
                                                .into(holder.getImg());
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                }

                                holder.getUserName().setText(user.getUserName());
                                holder.getUserStatus().setText(user.getUserStatus());

                                if (user.getUserPresence() == 1) {
                                    holder.getUserPresence().setImageResource(android.R.drawable.presence_online);
                                } else {
                                    holder.getUserPresence().setImageResource(android.R.drawable.presence_offline);
                                }

                                holder.getDeleteChat().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        deleteChat(position, user);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            mMessageReference
                    .child(chat.getChatId())
                    .orderByChild(Database.users.USER_ID)
                    .equalTo(chat.getUserIdAuth())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(messages != null) messages.clear();

                            for(DataSnapshot snapMessage : dataSnapshot.getChildren()){
                                Message message = snapMessage.getValue(Message.class);
                                if(!app.getmFirebaseAuth().getCurrentUser().getUid().equals(message.getUserId())
                                        && !message.isReadMessage()) messages.add(message);
                            }

                            if(messages.size() > 0){
                                holder.getChatUnread().setVisibility(View.VISIBLE);
                                holder.getChatUnread().setText(String.valueOf(messages.size()));
                            }else{
                                holder.getChatUnread().setVisibility(View.INVISIBLE);
                                holder.getChatUnread().setText("");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

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

    private void deleteChat(final int position, final User user){

        final Chat chat = chats.get(position);

        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.delete_chat_title))
                .setMessage(context.getResources().getString(R.string.delete_chat_message)
                        + user.getUserName() + context.getResources().getString(R.string.delete_chat_all_messages))
                .setPositiveButton(context.getResources().getString(R.string.delete_chat_positive),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                DatabaseReference mMessageDeleteReference = app.getmFirebaseDatabase().getReference()
                                        .child(Database.messages.CHILD_MESSAGES);

                                DatabaseReference mChatDeleteReference = app.getmFirebaseDatabase().getReference()
                                        .child(Database.chats.CHILD_CHATS)
                                        .child(app.getmAppUser().getUserKey());

                                DatabaseReference mUserChatDeleteReference = app.getmFirebaseDatabase().getReference()
                                        .child(Database.chats.CHILD_CHATS)
                                        .child(user.getUserKey());

                                mMessageDeleteReference.child(chat.getChatId()).removeValue();
                                mChatDeleteReference.child(chat.getChatPushKey()).removeValue();
                                mUserChatDeleteReference.child(chat.getChatPushKey()).removeValue();

                                chats.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, chats.size());
                            }
                        })
                .setNegativeButton(context.getResources().getString(R.string.delete_chat_negative), null)
                .show();
    }
}
