package io.github.tiagofrbarbosa.fleekard.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.Manifest;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.activity.prefs.SettingsActivity;
import io.github.tiagofrbarbosa.fleekard.adapter.ViewPagerAdapter;
import io.github.tiagofrbarbosa.fleekard.component.AppComponent;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.model.User;

/**
 * Created by tfbarbosa on 15/09/17.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.adView) AdView mAdView;

    @Inject Glide glide;

    private FleekardApplication app;
    private FirebaseUser mFirebaseUser;
    private Bundle extras;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        app = (FleekardApplication) getApplication();
        AppComponent component = app.getComponent();
        component.inject(this);

        mFirebaseUser = app.getmFirebaseAuth().getCurrentUser();

        if(getIntent().getExtras() != null) extras = getIntent().getExtras();

        MobileAds.initialize(this, getResources().getString(R.string.app_ad_id));

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("ABCDEF012345")
                .build();

        mAdView.loadAd(adRequest);

        setSupportActionBar(toolbar);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    public void setupTabIcons(){

            tabLayout.getTabAt(ViewPagerAdapter.TAB_USER)
                    .setIcon(R.drawable.ic_gps_fixed_white_24dp)
                    .setContentDescription(getResources().getString(R.string.content_desc_tab_user));

            tabLayout.getTabAt(ViewPagerAdapter.TAB_NOTIFICATION)
                    .setIcon(R.drawable.ic_notifications_white_24dp)
                    .setContentDescription(getResources().getString(R.string.content_desc_tab_notification));

            tabLayout.getTabAt(ViewPagerAdapter.TAB_CHAT)
                    .setIcon(R.drawable.ic_question_answer_white_24dp)
                    .setContentDescription(getResources().getString(R.string.content_desc_tab_chats));

            tabLayout.getTabAt(ViewPagerAdapter.TAB_FAVORITE)
                    .setIcon(R.drawable.ic_favorite_white_24dp)
                    .setContentDescription(getResources().getString(R.string.content_desc_tab_favorite));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        String userProfileKey;

        if(getIntent().getExtras() != null){
            userProfileKey = getIntent().getExtras().getString(Database.users.USER_KEY);
        }else{
            userProfileKey = app.getmAppUser().getUserKey();
        }

        if (id == R.id.profile_settings) {

            DatabaseReference mUserReference = app.getmFirebaseDatabase().getReference()
                    .child(Database.users.CHILD_USERS);

            mUserReference
                    .orderByChild(Database.users.USER_KEY)
                    .equalTo(userProfileKey)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                              @Override
                              public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot userSnap : dataSnapshot.getChildren()){
                                        User user = userSnap.getValue(User.class);

                                        Intent intent = new Intent(MainActivity.this, ProfileEditActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString(Database.users.USER_ID, user.getUserId());
                                        bundle.putString(Database.users.USER_KEY, user.getUserKey());
                                        bundle.putString(Database.users.USER_NAME, user.getUserName());
                                        bundle.putString(Database.users.USER_STATUS, user.getUserStatus());
                                        bundle.putString(Database.users.USER_IMAGE, user.getImg());
                                        bundle.putString(Database.users.USER_EMAIL, user.getEmail());
                                        bundle.putString(Database.users.USER_GENDER, String.valueOf(user.getGender()));
                                        bundle.putString(Database.users.USER_AGE, String.valueOf(user.getAge()));
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        finish();
                                    }
                              }

                              @Override
                              public void onCancelled(DatabaseError databaseError) {

                              }
                          });
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        if (id == R.id.menu_exit) {

            String userKey = app.getmAppUser().getUserKey();

            DatabaseReference mPresenceReference = app.getmFirebaseDatabase().getReference()
                    .child(Database.users.CHILD_USERS)
                    .child(userKey)
                    .child(Database.users.USER_PRESENCE);

            mPresenceReference.setValue(User.USER_DISCONNECTED);

            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivity(new Intent(MainActivity.this, SignIn.class));
                            finish();
                        }
                    });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Concedida!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Negada!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
