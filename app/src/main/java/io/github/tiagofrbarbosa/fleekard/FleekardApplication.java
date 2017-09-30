package io.github.tiagofrbarbosa.fleekard;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;

import io.github.tiagofrbarbosa.fleekard.component.AppComponent;
import io.github.tiagofrbarbosa.fleekard.component.DaggerAppComponent;
import io.github.tiagofrbarbosa.fleekard.module.AppModule;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 15/09/17.
 */

public class FleekardApplication extends Application {
    private AppComponent component;
    private FirebaseDatabase mFirebaseDatabase;

    @Override
    public void onCreate(){
        super.onCreate();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }

        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getComponent(){
        return component;
    }

    public FirebaseDatabase getmFirebaseDatabase(){
        return mFirebaseDatabase;
    }
}
