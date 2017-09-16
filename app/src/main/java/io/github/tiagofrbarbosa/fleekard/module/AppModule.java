package io.github.tiagofrbarbosa.fleekard.module;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tfbarbosa on 16/09/17.
 */

@Module
public class AppModule {

    private Application app;

    public AppModule(Application app){
        this.app = app;
    }

    @Provides
    public Glide getGlide(){
        Glide glide = new GlideBuilder().build(app);
        return glide;
    }
}
