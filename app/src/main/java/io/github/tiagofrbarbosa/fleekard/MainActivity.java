package io.github.tiagofrbarbosa.fleekard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 15/09/17.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mybutton) Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Timber.e("Timber test");
    }

    @OnClick(R.id.mybutton)
    public void myButtonClick(){
        Toast.makeText(this,"Butter test",Toast.LENGTH_LONG).show();
    }
}
