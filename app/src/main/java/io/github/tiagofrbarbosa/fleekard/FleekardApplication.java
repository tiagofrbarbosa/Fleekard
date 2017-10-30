package io.github.tiagofrbarbosa.fleekard;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import io.github.tiagofrbarbosa.fleekard.component.AppComponent;
import io.github.tiagofrbarbosa.fleekard.component.DaggerAppComponent;
import io.github.tiagofrbarbosa.fleekard.model.User;
import io.github.tiagofrbarbosa.fleekard.module.AppModule;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 15/09/17.
 */

public class FleekardApplication extends Application {
    private AppComponent component;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseDatabase mFirebaseDatabasePersistence;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private User mAppUser;

    @Override
    public void onCreate(){
        super.onCreate();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabasePersistence = FirebaseDatabase.getInstance();
        mFirebaseDatabasePersistence.setPersistenceEnabled(true);
        mFirebaseStorage = FirebaseStorage.getInstance();
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

    public FirebaseDatabase getmFirebaseDatabasePersistence(){
        return mFirebaseDatabasePersistence;
    }

    public FirebaseAuth getmFirebaseAuth(){
        return mFirebaseAuth;
    }

    public FirebaseStorage getmFirebaseStorage(){
        return mFirebaseStorage;
    }

    public void setmAppUser(User mAppUser){
        this.mAppUser = mAppUser;
    }

    public User getmAppUser(){
        return this.mAppUser;
    }
}
