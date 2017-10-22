package io.github.tiagofrbarbosa.fleekard.component;

import dagger.Component;
import io.github.tiagofrbarbosa.fleekard.activity.MainActivity;
import io.github.tiagofrbarbosa.fleekard.activity.ProfileActivity;
import io.github.tiagofrbarbosa.fleekard.module.AppModule;

/**
 * Created by tfbarbosa on 16/09/17.
 */


@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MainActivity activity);
    void inject(ProfileActivity profile_activity);
}
