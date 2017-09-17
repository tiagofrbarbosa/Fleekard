package io.github.tiagofrbarbosa.fleekard.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.github.tiagofrbarbosa.fleekard.R;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class ChatActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}
