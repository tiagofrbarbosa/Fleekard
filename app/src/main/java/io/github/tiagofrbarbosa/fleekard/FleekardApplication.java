package io.github.tiagofrbarbosa.fleekard;

import android.app.Application;
import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(){
        super.onCreate();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

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

    public FirebaseAuth getmFirebaseAuth(){
        return mFirebaseAuth;
    }

    public FirebaseAnalytics getmFirebaseAnalytics(){
        return mFirebaseAnalytics;
    }
}
