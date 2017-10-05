package io.github.tiagofrbarbosa.fleekard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import io.github.tiagofrbarbosa.fleekard.BuildConfig;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.model.User;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 24/09/17.
 */

public class SignIn extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FleekardApplication app;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin);

        app = (FleekardApplication) getApplication();
        mFirebaseAuth = app.getmFirebaseAuth();
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if(mFirebaseUser != null){
                    onSignedInInitialize(mFirebaseUser);
                }else{
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .setTosUrl(getResources().getString(R.string.tos_link))
                                    .setPrivacyPolicyUrl(getResources().getString(R.string.privacy_policy_link))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    private void onSignedInInitialize(final FirebaseUser mFirebaseUser){

        Timber.tag("myLogin");

        final DatabaseReference mUserReference = app.getmFirebaseDatabase().getReference()
                .child(Database.users.CHILD_USERS);

        mUserReference
                .orderByChild(Database.users.USER_EMAIL)
                .equalTo(mFirebaseUser.getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getValue() == null){
                            Timber.i("User not exists: " + mFirebaseUser.getEmail());

                            DatabaseReference userKeyReference = app.getmFirebaseDatabase().getReference()
                                    .child(Database.users.CHILD_USERS);

                            String userKey = userKeyReference.push().getKey();

                            User mUser = new User(mFirebaseUser.getUid()
                                    , userKey
                                    , mFirebaseUser.getDisplayName()
                                    , getResources().getString(R.string.default_status)
                                    , Database.users.USER_IMAGE_AVATAR
                                    , mFirebaseUser.getEmail()
                                    , 0
                                    , 0
                                    , 0);

                            mUserReference
                                    .child(userKey).setValue(mUser);

                            Intent intent = new Intent(SignIn.this, ProfileEditActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(Database.users.USER_ID, mUser.getUserId());
                            bundle.putString(Database.users.USER_KEY, mUser.getUserKey());
                            bundle.putString(Database.users.USER_NAME, mUser.getUserName());
                            bundle.putString(Database.users.USER_STATUS, mUser.getUserStatus());
                            bundle.putString(Database.users.USER_IMAGE, mUser.getImg());
                            bundle.putString(Database.users.USER_GENDER, String.valueOf(mUser.getGender()));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else{

                            for(DataSnapshot snapUser : dataSnapshot.getChildren()){
                                User mSnapUser = snapUser.getValue(User.class);
                                Timber.i("User exists: " + mSnapUser.getEmail());

                                Intent intent = new Intent(SignIn.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(Database.users.USER_ID, mSnapUser.getUserId());
                                bundle.putString(Database.users.USER_KEY, mSnapUser.getUserKey());
                                bundle.putString(Database.users.USER_NAME, mSnapUser.getUserName());
                                bundle.putString(Database.users.USER_STATUS, mSnapUser.getUserStatus());
                                bundle.putString(Database.users.USER_IMAGE, mSnapUser.getImg());
                                bundle.putString(Database.users.USER_GENDER, String.valueOf(mSnapUser.getGender()));
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.i("Timber_error: " + databaseError.getMessage());
                    }
                });
    }

    private void onSignedOutCleanUp(){
    }

    @Override
    protected void onResume(){
        super.onResume();
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(mAuthStateListener != null){
          mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
