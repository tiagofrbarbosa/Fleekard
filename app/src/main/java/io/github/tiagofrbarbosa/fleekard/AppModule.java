package io.github.tiagofrbarbosa.fleekard;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.squareup.picasso.Picasso;

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
    public Picasso getPicasso(){
        Picasso picasso = new Picasso.Builder(app).build();
        return picasso;
    }

    @Provides
    public Glide getGlide(){
        Glide glide = new GlideBuilder().build(app);
        return glide;
    }
}
