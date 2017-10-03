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
    protected DatabaseReference mChatReference;

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

        mChatReference = app.getmFirebaseDatabase().getReference()
                .child(Database.chats.CHILD_CHATS)
                .child(mFirebaseUser.getUid());

        mChatReference
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot chatSnap : dataSnapshot.getChildren()){
                            Chat chat = chatSnap.getValue(Chat.class);
                            chats.add(chat);
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                Chat c = chats.get(idx);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        };
    }
}
