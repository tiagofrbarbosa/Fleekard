package io.github.tiagofrbarbosa.fleekard;

import dagger.Component;

/**
 * Created by tfbarbosa on 16/09/17.
 */


@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MainActivity activity);
}
