package io.github.tiagofrbarbosa.fleekard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.activity.ChatActivity;
import io.github.tiagofrbarbosa.fleekard.adapter.ChatAdapter;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.model.Chat;
import io.github.tiagofrbarbosa.fleekard.model.User;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class FragmentChat extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    protected ChatAdapter adapter;
    protected List<Chat> chats;
    protected FleekardApplication app;
    protected FirebaseUser mFirebaseUser;
    protected DatabaseReference mUserReference;
    protected DatabaseReference mUserConnectedReference;
    protected DatabaseReference mChatReference;
    protected DatabaseReference mPresenceReference;
    protected DatabaseReference mConnectReference;
    protected User userConnected;
    protected Chat c;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

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

        mChatReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(chats != null) chats.clear();

                        for(DataSnapshot chatSnap : dataSnapshot.getChildren()){
                            Chat chat = chatSnap.getValue(Chat.class);
                            chats.add(chat);
                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        layoutManager.setReverseLayout(true);
                        layoutManager.setStackFromEnd(true);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter = new ChatAdapter(getActivity(), chats, onClickChat(), mUserReference));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    protected ChatAdapter.ChatOnclickListener onClickChat(){

        return new ChatAdapter.ChatOnclickListener(){

            @Override
            public void onClickChat(ChatAdapter.ChatsViewHolder holder, int idx) {
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
