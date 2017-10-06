package io.github.tiagofrbarbosa.fleekard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.model.Chat;
import io.github.tiagofrbarbosa.fleekard.model.User;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.profile_image) ImageView profileImage;
    @BindView(R.id.user_status) TextView userStatus;
    @BindView(R.id.user_age) TextView userAge;
    @BindView(R.id.user_gender) ImageView userGender;
    @BindView(R.id.user_chat) ImageView userChat;

    @Inject
    Glide glide;

    private FleekardApplication app;
    private DatabaseReference mUserReference;
    private DatabaseReference mUserConnectedReference;
    private DatabaseReference mChatReference;
    private DatabaseReference mChatValidationReference;
    private FirebaseUser mFirebaseUser;
    private Bundle extras;
    private User user;
    private User userConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        app = (FleekardApplication) getApplication();

        mFirebaseUser = app.getmFirebaseAuth().getCurrentUser();



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().getExtras() != null) {
            extras = getIntent().getExtras();

            mUserReference = app.getmFirebaseDatabase().getReference()
                    .child(Database.users.CHILD_USERS);

            mUserReference
                    .orderByChild(Database.users.USER_KEY)
                    .equalTo(extras.getString(Database.users.USER_KEY))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot userSnap : dataSnapshot.getChildren()){
                                user = userSnap.getValue(User.class);

                                userName.setText(user.getUserName());

                                glide.with(ProfileActivity.this)
                                        .load(user.getImg())
                                        .apply(RequestOptions.placeholderOf(R.drawable.user_avatar))
                                        .into(profileImage);

                                userStatus.setText(user.getUserStatus());
                                userAge.setText(String.valueOf(user.getAge()));

                                if(user.getGender() == 0){
                                    userGender.setImageResource(R.drawable.ic_male);
                                }else{
                                    userGender.setImageResource(R.drawable.ic_female);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

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
    }

    @OnClick(R.id.user_chat)
    public void onClickUserChat(){

        mChatReference = app.getmFirebaseDatabase().getReference()
                .child(Database.chats.CHILD_CHATS);

        mChatValidationReference = app.getmFirebaseDatabase().getReference()
                .child(Database.chats.CHILD_CHATS)
                .child(userConnected.getUserKey());

        final String chatId = userConnected.getUserKey() + user.getUserKey();

        mChatValidationReference
                .orderByChild(Database.users.USER_KEY)
                .equalTo(user.getUserKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() == null){
                                Chat chat = new Chat(chatId, user.getUserKey(), 1, 10);
                                Chat chatCrush = new Chat(chatId, userConnected.getUserKey(), 1, 10);

                                mChatReference.child(userConnected.getUserKey()).push().setValue(chat);
                                mChatReference.child(user.getUserKey()).push().setValue(chatCrush);
                            }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Database.chats.CHAT_ID, chatId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_exit) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
