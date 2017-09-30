package io.github.tiagofrbarbosa.fleekard.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.database.Database;
import io.github.tiagofrbarbosa.fleekard.model.User;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class ProfileEditActivity extends AppCompatActivity{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.profile_image) ImageView profileImage;
    @BindView(R.id.user_name) EditText userName;
    @BindView(R.id.user_status) EditText userStatus;
    @BindView(R.id.user_gender) Spinner spinner;
    @BindView(R.id.user_age) EditText userAge;
    @BindView(R.id.fab_save) FloatingActionButton floatingActionButton;

    @Inject
    Glide glide;

    private FleekardApplication app;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        app = (FleekardApplication) getApplication();

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.gender_array,
                        android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        floatingActionButton.setVisibility(View.GONE);

        if(getIntent().getExtras() != null) {
            extras = getIntent().getExtras();
            userName.setText(extras.getString(Database.users.USER_NAME));
            userStatus.setText(extras.getString(Database.users.USER_STATUS));
            floatingActionButton.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.fab_save)
    public void onClick(){

        if(extras != null) {
            DatabaseReference mUserReference = app.getmFirebaseDatabase()
                    .getReference()
                    .child(Database.users.CHILD_USERS)
                    .child(extras.getString(Database.users.USER_ID));


            int gender = spinner.getSelectedItem().toString().equals("Male") ? User.GENDER_VALUE_MALE : User.GENDER_VALUE_FEMALE;
            int userAgeInt = Integer.valueOf(userAge.getText().toString());

            User user = new User(userName.getText().toString(), userStatus.getText().toString(),
                    gender, userAgeInt);

            mUserReference.child(Database.users.USER_NAME).setValue(user.getUserName());
            mUserReference.child(Database.users.USER_STATUS).setValue(user.getUserStatus());
            mUserReference.child(Database.users.USER_GENDER).setValue(user.getGender());
            mUserReference.child(Database.users.USER_AGE).setValue(user.getAge());
        }

        finish();
    }
}
