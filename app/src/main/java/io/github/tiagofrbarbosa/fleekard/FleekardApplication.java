package io.github.tiagofrbarbosa.fleekard;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by tfbarbosa on 15/09/17.
 */

public class FleekardApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
    }
}
