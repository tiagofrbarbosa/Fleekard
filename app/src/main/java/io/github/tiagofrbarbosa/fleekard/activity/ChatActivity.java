package io.github.tiagofrbarbosa.fleekard.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.adapter.MessageChatAdapter;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Storage;
import io.github.tiagofrbarbosa.fleekard.model.Message;
import io.github.tiagofrbarbosa.fleekard.model.User;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class ChatActivity extends AppCompatActivity {

    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int RC_PHOTO_PICKER = 2;

    @BindView(R.id.header) ImageView mHeader;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.photoPickerButton) ImageButton mPhotoPickerButton;
    @BindView(R.id.messageEditText) EditText mMessageEditText;
    @BindView(R.id.sendButton) Button mSendButton;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;

    @Inject Glide glide;

    private MessageChatAdapter mMessageChatAdapter;
    private List<Message> mMessages;
    private String mUserName;

    private FleekardApplication app;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseDatabase mFirebaseDatabasePersistence;
    private DatabaseReference mMessageDatabaseReference;
    private DatabaseReference mUserDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;

    private Bundle extras;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_paralax);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        app = (FleekardApplication) getApplication();

        mFirebaseDatabase = app.getmFirebaseDatabase();
        mFirebaseDatabasePersistence = app.getmFirebaseDatabasePersistence();
        mFirebaseAuth = app.getmFirebaseAuth();
        mFirebaseStorage = app.getmFirebaseStorage();

        if(getIntent().getExtras() != null){
            extras = getIntent().getExtras();

            glide.with(this)
                    .load(extras.getString(Database.users.USER_IMAGE))
                    .into(mHeader);

            collapsingToolbarLayout.setTitle(extras.getString(Database.users.USER_NAME));
        }

        mMessageDatabaseReference = mFirebaseDatabasePersistence.getReference()
                .child(Database.messages.CHILD_MESSAGES)
                .child(extras.getString(Database.chats.CHAT_ID));

        mUserDatabaseReference = mFirebaseDatabase.getReference()
                .child(Database.users.CHILD_USERS);

        mChatPhotosStorageReference = mFirebaseStorage.getReference()
                .child(Storage.messages.MESSAGES_IMAGE_PATH)
                .child(extras.getString(Database.chats.CHAT_ID));

        attachDatabaseReadListener();

        mMessages = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mMessageChatAdapter = new MessageChatAdapter(this, mMessages, onClickMessage()));

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

    @OnClick(R.id.photoPickerButton)
    public void onClickPicker(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.photo_picker_intent)), RC_PHOTO_PICKER);
    }

    @OnClick(R.id.sendButton)
    public void onClickSendButton(){

        Message mMessage = new Message(mMessageEditText.getText().toString(), mUserName, null);
        mMessageDatabaseReference.push().setValue(mMessage);
        mMessageEditText.setText("");

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mMessageEditText.getWindowToken(), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();

            StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            Message mMessage = new Message(null, mUserName, downloadUrl.toString());
                            mMessageDatabaseReference.push().setValue(mMessage);
                        }
                    });
        }
    }

    private void attachDatabaseReadListener(){
        if(mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Message mMessage = dataSnapshot.getValue(Message.class);
                    mMessages.add(mMessage);
                    mRecyclerView.setAdapter(mMessageChatAdapter = new MessageChatAdapter(ChatActivity.this, mMessages, onClickMessage()));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mMessageDatabaseReference.addChildEventListener(mChildEventListener);
        }

        if(mValueEventListener == null){
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot userSnap : dataSnapshot.getChildren()){
                        User user = userSnap.getValue(User.class);
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

    protected MessageChatAdapter.MessageOnclickListener onClickMessage(){

        return new MessageChatAdapter.MessageOnclickListener(){
            @Override
            public void onClickUser(MessageChatAdapter.MessagesViewHolder holder, int idx) {
            }
        };
    }
}
