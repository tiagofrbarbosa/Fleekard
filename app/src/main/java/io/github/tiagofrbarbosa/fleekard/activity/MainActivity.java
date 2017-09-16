package io.github.tiagofrbarbosa.fleekard.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.component.AppComponent;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 15/09/17.
 */

public class MainActivity extends AppCompatActivity {

    private AppComponent component;

    @Inject Glide glide;

    @BindView(R.id.mybutton) Button b;
    @BindView(R.id.myImageView) ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Timber.e("Timber test");

        FleekardApplication app = (FleekardApplication) getApplication();
        component = app.getComponent();
        component.inject(this);

        glide.with(this).load("http://i.imgur.com/DvpvklR.png").into(imageView);
    }

    @OnClick(R.id.mybutton)
    public void myButtonClick(){
        Toast.makeText(this,"Butter test",Toast.LENGTH_LONG).show();
    }
}
