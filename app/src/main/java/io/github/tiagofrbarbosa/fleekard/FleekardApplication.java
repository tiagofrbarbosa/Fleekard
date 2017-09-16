package io.github.tiagofrbarbosa.fleekard;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by tfbarbosa on 15/09/17.
 */

public class FleekardApplication extends Application {
    private AppComponent component;

    @Override
    public void onCreate(){
        super.onCreate();

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
}
