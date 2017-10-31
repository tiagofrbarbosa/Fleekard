package io.github.tiagofrbarbosa.fleekard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.activity.ChatActivity;
import io.github.tiagofrbarbosa.fleekard.adapter.ChatAdapter;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.holder.ChatsViewHolder;
import io.github.tiagofrbarbosa.fleekard.model.Chat;
import io.github.tiagofrbarbosa.fleekard.model.User;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class FragmentChat extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.myProgressBar) ProgressBar progressBar;

    protected ChatAdapter adapter;
    protected ArrayList<Chat> chats;
    protected FleekardApplication app;
    protected FirebaseUser mFirebaseUser;
    protected DatabaseReference mUserReference;
    protected DatabaseReference mUserConnectedReference;
    protected DatabaseReference mChatReference;
    protected DatabaseReference mPresenceReference;
    protected DatabaseReference mConnectReference;
    protected User userConnected;
    protected Chat c;

    private Parcelable parcelable;
    private static final String RECYCLER_LIST_SATE = "recycler_list_state";
    private static final String CHAT_PARCELABLE = "chat_parcelable";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        app = (FleekardApplication) getActivity().getApplication();

        mFirebaseUser = app.getmFirebaseAuth().getCurrentUser();

        chats = Chat.getChats();

        mUserReference = app.getmFirebaseDatabase().getReference()
                .child(Database.users.CHILD_USERS);

        mUserConnectedReference = app.getmFirebaseDatabase().getReference()
                .child(Database.users.CHILD_USERS);

        mUserConnectedReference
                .orderByChild(Database.users.USER_ID)
                .equalTo(mFirebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot userSnap : dataSnapshot.getChildren()){
                            userConnected = userSnap.getValue(User.class);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        String userKey = app.getmAppUser().getUserKey();

        mChatReference = app.getmFirebaseDatabase().getReference()
                .child(Database.chats.CHILD_CHATS)
                .child(userKey);

        mPresenceReference = app.getmFirebaseDatabase().getReference()
                .child(Database.users.CHILD_USERS)
                .child(userKey)
                .child(Database.users.USER_PRESENCE);

        mConnectReference = app.getmFirebaseDatabase().getReference(".info/connected");

        mConnectReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);

                if(connected){
                    mPresenceReference.setValue(User.USER_CONNECTED);
                    mPresenceReference.onDisconnect().setValue(User.USER_DISCONNECTED);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(savedInstanceState == null) {

            progressBar.setVisibility(View.VISIBLE);

            mChatReference
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (chats != null) chats.clear();

                            for (DataSnapshot chatSnap : dataSnapshot.getChildren()) {
                                Chat chat = chatSnap.getValue(Chat.class);
                                chats.add(chat);
                            }

                            recyclerView.setAdapter(adapter = new ChatAdapter(getActivity(), chats, onClickChat(), app));
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }else{

            progressBar.setVisibility(View.INVISIBLE);
            chats = savedInstanceState.getParcelableArrayList(CHAT_PARCELABLE);
            recyclerView.setAdapter(adapter = new ChatAdapter(getActivity(), chats, onClickChat(), app));
            parcelable = savedInstanceState.getParcelable(RECYCLER_LIST_SATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(parcelable);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        parcelable = recyclerView.getLayoutManager().onSaveInstanceState();
        savedInstanceState.putParcelable(RECYCLER_LIST_SATE, parcelable);
        savedInstanceState.putParcelableArrayList(CHAT_PARCELABLE, chats);
    }

    protected ChatAdapter.ChatOnclickListener onClickChat(){

        return new ChatAdapter.ChatOnclickListener(){

            @Override
            public void onClickChat(ChatsViewHolder holder, int idx) {
                c = chats.get(idx);

                mUserReference
                        .orderByChild(Database.users.USER_KEY)
                        .equalTo(c.getUserId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot userSnap : dataSnapshot.getChildren()){
                                    User user = userSnap.getValue(User.class);
                                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Database.chats.CHAT_ID, c.getChatId());
                                    bundle.putString(Database.users.USER_KEY, user.getUserKey());
                                    bundle.putString(Database.users.USER_NAME, user.getUserName());
                                    bundle.putString(Database.users.USER_IMAGE, user.getImg());
                                    bundle.putString(Database.users.USER_NOTIFICATION_TOKEN, user.getNotificationToken().getToken());
                                    bundle.putInt(Database.users.USER_PRESENCE, user.getUserPresence());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        };
    }
}