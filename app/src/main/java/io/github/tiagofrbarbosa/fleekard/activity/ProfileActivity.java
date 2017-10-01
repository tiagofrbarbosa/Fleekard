package io.github.tiagofrbarbosa.fleekard.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
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

    @Inject
    Glide glide;

    private FleekardApplication app;
    private DatabaseReference mUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        app = (FleekardApplication) getApplication();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();

            mUserReference = app.getmFirebaseDatabase().getReference()
                    .child(Database.users.CHILD_USERS);

            mUserReference
                    .orderByChild(Database.users.USER_ID)
                    .equalTo(extras.getString(Database.users.USER_ID))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot userSnap : dataSnapshot.getChildren()){
                                User user = userSnap.getValue(User.class);

                                userName.setText(user.getUserName());
                                glide.with(ProfileActivity.this).load(user.getImg()).into(profileImage);
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
