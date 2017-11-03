package io.github.tiagofrbarbosa.fleekard.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.adapter.MessageChatAdapter;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Storage;
import io.github.tiagofrbarbosa.fleekard.holder.MessagesViewHolder;
import io.github.tiagofrbarbosa.fleekard.model.Message;
import io.github.tiagofrbarbosa.fleekard.model.Notification;
import io.github.tiagofrbarbosa.fleekard.model.User;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class ChatActivity extends AppCompatActivity {

    public static final int DEFAULT_MSG_LENGTH_LIMIT = 500;
    private static final int RC_PHOTO_PICKER = 2;

    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.photoPickerButton) ImageButton mPhotoPickerButton;
    @BindView(R.id.messageEditText) EditText mMessageEditText;
    @BindView(R.id.sendButton) Button mSendButton;

    @Inject Glide glide;

    private MessageChatAdapter mMessageChatAdapter;
    private ArrayList<Message> mMessages;
    private String mUserId;
    private String mUserName;
    private LinearLayoutManager linearLayoutManager;

    private FleekardApplication app;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseDatabase mFirebaseDatabasePersistence;
    private DatabaseReference mMessageDatabaseReference;
    private DatabaseReference mUserDatabaseReference;
    private ValueEventListener mMessageEventListener;
    private ValueEventListener mValueEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;

    private Bundle extras;

    private Parcelable parcelable;
    private static final String RECYCLER_LIST_SATE = "recycler_list_state";
    private static final String MESSAGE_PARCELABLE = "message_parcelable";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        app = (FleekardApplication) getApplication();

        mFirebaseDatabase = app.getmFirebaseDatabase();
        mFirebaseDatabasePersistence = app.getmFirebaseDatabasePersistence();
        mFirebaseAuth = app.getmFirebaseAuth();
        mFirebaseStorage = app.getmFirebaseStorage();

        if(getIntent().getExtras() != null) extras = getIntent().getExtras();

        mMessageDatabaseReference = mFirebaseDatabasePersistence.getReference()
                .child(Database.messages.CHILD_MESSAGES)
                .child(extras.getString(Database.chats.CHAT_ID));

        mUserDatabaseReference = mFirebaseDatabase.getReference()
                .child(Database.users.CHILD_USERS);

        mChatPhotosStorageReference = mFirebaseStorage.getReference()
                .child(Storage.messages.MESSAGES_IMAGE_PATH)
                .child(extras.getString(Database.chats.CHAT_ID));

        mMessages = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mMessageChatAdapter = new MessageChatAdapter(this, mMessages, onClickMessage(), app));

        if(savedInstanceState == null) attachDatabaseReadListener();

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() > 0){
                    mSendButton.setEnabled(true);
                }else{
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mProgressBar.getVisibility() == View.VISIBLE) mProgressBar.setVisibility(View.INVISIBLE);
        detachDatabaseReadLIstener();
    }

    @Override
    public void onStart(){
        super.onStart();
        attachDatabaseReadListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        parcelable = mRecyclerView.getLayoutManager().onSaveInstanceState();
        savedInstanceState.putParcelable(RECYCLER_LIST_SATE, parcelable);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            parcelable = savedInstanceState.getParcelable(RECYCLER_LIST_SATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(parcelable);
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

    @OnClick(R.id.photoPickerButton)
    public void onClickPicker(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.photo_picker_intent)), RC_PHOTO_PICKER);
    }

    @OnClick(R.id.sendButton)
    public void onClickSendButton(){

        Message mMessage = new Message(mMessageEditText.getText().toString().trim(), mUserId, mUserName, null, false);
        mMessageDatabaseReference.push().setValue(mMessage);
        mMessageEditText.setText("");

        if(extras.getInt(Database.users.USER_PRESENCE) == User.USER_DISCONNECTED) {
            Notification mNotificationMessage = new Notification(app.getmAppUser().getUserKey()
                    , extras.getString(Database.users.USER_KEY)
                    , extras.getString(Database.users.USER_NOTIFICATION_TOKEN)
                    , app.getmAppUser().getUserId()
                    , Notification.INTERACTION_CODE_MSG);

            DatabaseReference mNotificationReference = app.getmFirebaseDatabase().getReference()
                    .child(Database.notification_message.CHILD_NOTIFICATION_MESSAGE)
                    .child(extras.getString(Database.users.USER_KEY))
                    .child(app.getmAppUser().getUserKey());

            mNotificationReference.setValue(mNotificationMessage);
        }

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mMessageEditText.getWindowToken(), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){

            Uri selectedImageUri = data.getData();

            StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            mProgressBar.setVisibility(View.VISIBLE);

            photoRef.putFile(selectedImageUri)
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    })
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            Message mMessage = new Message(null, mUserId, mUserName, downloadUrl.toString(), false);
                            mMessageDatabaseReference.push().setValue(mMessage);
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }

    private void attachDatabaseReadListener(){

        if(mMessageEventListener == null){
            mMessageEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(mMessages != null) mMessages.clear();

                    for(DataSnapshot snapMessage : dataSnapshot.getChildren()){

                        GenericTypeIndicator<HashMap<String, Object>> mGenericHash = new GenericTypeIndicator<HashMap<String, Object>>() {};

                        Message mMessage = new Message();
                        mMessage.setText(snapMessage.child("text").getValue(String.class));
                        mMessage.setUserId(snapMessage.child("userId").getValue(String.class));
                        mMessage.setName(snapMessage.child("name").getValue(String.class));
                        mMessage.setPhotoUrl(snapMessage.child("photoUrl").getValue(String.class));
                        mMessage.setmTimeStamp(snapMessage.child("mTimeStamp").getValue(mGenericHash));
                        mMessage.setReadMessage(snapMessage.child("readMessage").getValue(Boolean.class));

                        if(!mFirebaseAuth.getCurrentUser().getUid().equals(mMessage.getUserId()) && !mMessage.isReadMessage()){
                            snapMessage.getRef().child(Database.messages.MESSAGE_READ).setValue(true);
                        }

                        mMessages.add(mMessage);
                    }
                    mRecyclerView.setAdapter(mMessageChatAdapter = new MessageChatAdapter(ChatActivity.this, mMessages, onClickMessage(), app));

                    if(parcelable != null)
                        mRecyclerView.getLayoutManager().onRestoreInstanceState(parcelable);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mMessageDatabaseReference.addValueEventListener(mMessageEventListener);
        }

        if(mValueEventListener == null){
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot userSnap : dataSnapshot.getChildren()){
                        User user = userSnap.getValue(User.class);
                        mUserId = user.getUserId();
                        mUserName = user.getUserName();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mUserDatabaseReference
                    .orderByChild(Database.users.USER_ID)
                    .equalTo(mFirebaseAuth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(mValueEventListener);
        }
    }

    private void detachDatabaseReadLIstener(){
        if(mMessageEventListener != null) {
            mMessageDatabaseReference.removeEventListener(mMessageEventListener);
            mMessageEventListener = null;
        }

        parcelable = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mMessages.clear();
    }

    protected MessageChatAdapter.MessageOnclickListener onClickMessage(){

        return new MessageChatAdapter.MessageOnclickListener(){
            @Override
            public void onClickUser(MessagesViewHolder holder, int idx) {
            }
        };
    }
}
