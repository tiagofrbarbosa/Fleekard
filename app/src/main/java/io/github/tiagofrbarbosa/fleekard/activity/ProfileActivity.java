package io.github.tiagofrbarbosa.fleekard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.google.firebase.database.ChildEventListener;
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
import io.github.tiagofrbarbosa.fleekard.model.Favorite;
import io.github.tiagofrbarbosa.fleekard.model.User;
import timber.log.Timber;

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
    @BindView(R.id.user_like) ImageView userLike;
    @BindView(R.id.user_chat) ImageView userChat;

    @Inject
    Glide glide;

    private FleekardApplication app;
    private DatabaseReference mUserReference;
    private DatabaseReference mUserConnectedReference;
    private DatabaseReference mChatReference;
    private DatabaseReference mChatValidationReference;
    private DatabaseReference mFavoriteReference;
    private FirebaseUser mFirebaseUser;
    private Bundle extras;
    private User user;
    private User userConnected;
    private String chatId;

    private static final String USER_LIKE_UNCHECKED = "0";
    private static final String USER_LIKE_CHECKED = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        app = (FleekardApplication) getApplication();

        mFirebaseUser = app.getmFirebaseAuth().getCurrentUser();

        setSupportActionBar(toolbar);

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

                            mFavoriteReference = app.getmFirebaseDatabase().getReference()
                                    .child(Database.favorite.CHILD_FAVORITES)
                                    .child(userConnected.getUserKey());

                            userLikeVerify();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void userLikeVerify(){
        mFavoriteReference
                .orderByChild(Database.users.USER_KEY)
                .equalTo(user.getUserKey())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() != null){
                            userLike.setImageResource(R.drawable.ic_favorite_black_36dp);
                            userLike.setTag(USER_LIKE_CHECKED);
                        }else{
                            userLike.setImageResource(R.drawable.ic_favorite_border_black_36dp);
                            userLike.setTag(USER_LIKE_UNCHECKED);
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

        String userKey = user.getUserKey();

        mChatValidationReference
                .orderByChild(Database.users.USER_ID)
                .equalTo(userKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() == null){

                                chatId = userConnected.getUserKey() + user.getUserKey();
                                Chat chat = new Chat(chatId, user.getUserKey(), 1, 10);
                                Chat chatCrush = new Chat(chatId, userConnected.getUserKey(), 1, 10);

                                mChatReference.child(userConnected.getUserKey()).push().setValue(chat);
                                mChatReference.child(user.getUserKey()).push().setValue(chatCrush);

                                Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(Database.chats.CHAT_ID, chatId);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }else{
                                for(DataSnapshot chatSnap : dataSnapshot.getChildren()) {
                                    Chat mChat = chatSnap.getValue(Chat.class);
                                    chatId = mChat.getChatId();
                                }

                                Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(Database.chats.CHAT_ID, chatId);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                
                            }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @OnClick(R.id.user_like)
    public void onClickUserLike(){

        if(userLike.getTag().equals(USER_LIKE_UNCHECKED)){

            Favorite favorite = new Favorite(user.getUserKey(), "My fav");
            userLike.setImageResource(R.drawable.ic_favorite_black_36dp);
            userLike.setTag(USER_LIKE_CHECKED);
            mFavoriteReference.push().setValue(favorite);

        }else{
            userLike.setImageResource(R.drawable.ic_favorite_border_black_36dp);
            userLike.setTag(USER_LIKE_UNCHECKED);

            mFavoriteReference
                    .orderByChild(Database.users.USER_KEY)
                    .equalTo(user.getUserKey())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot favSnap : dataSnapshot.getChildren()){
                                favSnap.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

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
