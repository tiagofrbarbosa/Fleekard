package io.github.tiagofrbarbosa.fleekard.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.tiagofrbarbosa.fleekard.R;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class ProfileEditActivity extends AppCompatActivity{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.profile_image) ImageView profileImage;
    @BindView(R.id.user_name) EditText userName;
    @BindView(R.id.user_status) EditText userStatus;
    @BindView(R.id.user_gender) Spinner spinner;
    @BindView(R.id.fab_save) FloatingActionButton floatingActionButton;

    @Inject
    Glide glide;

    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String USER_STATUS = "userStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.gender_array,
                        android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        if(getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            userName.setText(extras.getString(USER_NAME));
            userStatus.setText(extras.getString(USER_STATUS));
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
        Toast.makeText(this, spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
    }
}
