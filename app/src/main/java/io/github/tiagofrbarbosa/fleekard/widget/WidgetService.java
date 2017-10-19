package io.github.tiagofrbarbosa.fleekard.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by tfbarbosa on 18/10/17.
 */

@SuppressWarnings("NewApi")
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        WidgetDataProvider dataProvider = new WidgetDataProvider(getApplicationContext(), intent);
        return dataProvider;
    }
}
