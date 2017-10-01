package io.github.tiagofrbarbosa.fleekard.activity;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Storage;
import io.github.tiagofrbarbosa.fleekard.model.User;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class ProfileEditActivity extends AppCompatActivity{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.profile_image) ImageView profileImage;
    @BindView(R.id.profile_edit) ImageView profileEdit;
    @BindView(R.id.user_name) EditText userName;
    @BindView(R.id.user_status) EditText userStatus;
    @BindView(R.id.user_gender) Spinner spinner;
    @BindView(R.id.user_age) EditText userAge;
    @BindView(R.id.fab_save) FloatingActionButton floatingActionButton;

    @Inject
    Glide glide;

    private FleekardApplication app;
    private FirebaseStorage mFirebaseStorage;
    private Bundle extras;

    private static final int RC_PHOTO_PICKER = 2;

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

            if(!extras.getString(Database.users.USER_IMAGE).equals(Database.users.USER_IAMGE_AVATAR)){

                glide.with(this)
                        .load(extras.getString(Database.users.USER_IMAGE))
                        .apply(RequestOptions.circleCropTransform())
                        .into(profileImage);
            }else{

                glide.with(this)
                        .load(R.drawable.user_avatar)
                        .apply(RequestOptions.circleCropTransform())
                        .into(profileImage);
            }

            if(extras.getString(Database.users.USER_GENDER) != null) {
                if (Integer.valueOf(extras.getString(Database.users.USER_GENDER)) == 0) {
                    spinner.setSelection(adapter.getPosition("Male"));
                } else {
                    spinner.setSelection(adapter.getPosition("Female"));
                }
            }

            if(extras.getString(Database.users.USER_AGE) != null)
                userAge.setText(extras.getString(Database.users.USER_AGE));

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

        if(userName.getText().toString().matches("")){
            userName.requestFocus();
            Toast.makeText(this, getResources().getString(R.string.toast_edittext_username), Toast.LENGTH_SHORT).show();
        }
        else if(userStatus.getText().toString().matches("")){
            userStatus.requestFocus();
            Toast.makeText(this, getResources().getString(R.string.toast_edittext_userstatus), Toast.LENGTH_SHORT).show();
        }
        else if(userAge.getText().toString().matches("")){
            userAge.requestFocus();
            Toast.makeText(this, getResources().getString(R.string.toast_edittext_userage), Toast.LENGTH_SHORT).show();
        }else {

            if (extras.getString(Database.users.USER_ID) != null) {
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

                Toast.makeText(this, getResources().getString(R.string.toast_user_data_update), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.profile_edit)
    public void onClickEdit(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent,getResources().getString(R.string.photo_picker_intent)), RC_PHOTO_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        mFirebaseStorage = app.getmFirebaseStorage();

        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();

            StorageReference profileImageRef = mFirebaseStorage.getReference()
                    .child(Storage.users.USER_PROFILE_IMAGE_PATH)
                    .child(extras.getString(Database.users.USER_ID))
                    .child(Storage.users.USER_PROFILE_IMAGE);

            final DatabaseReference mUserReference = app.getmFirebaseDatabase()
                    .getReference()
                    .child(Database.users.CHILD_USERS)
                    .child(extras.getString(Database.users.USER_ID));

            profileImageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            User user = new User();
                            user.setImg(downloadUrl.toString());
                            mUserReference.child(Database.users.USER_IMAGE).setValue(user.getImg());

                            glide.with(ProfileEditActivity.this)
                                    .load(user.getImg())
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(profileImage);
                        }
                    });
        }
    }
}
