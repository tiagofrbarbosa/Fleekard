package io.github.tiagofrbarbosa.fleekard.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfbarbosa on 18/10/17.
 */


@SuppressWarnings("NewApi")
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    List mCollections = new ArrayList();
    Context mContext = null;

    public WidgetDataProvider(Context context, Intent intent){
        mContext = context;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    private void initData(){
        mCollections.clear();
        for(int i=1;i<=10;i++) mCollections.add("Text: " + i);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mCollections.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews mView = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
        mView.setTextViewText(android.R.id.text1, mCollections.get(i).toString());
        mView.setTextColor(android.R.id.text1, Color.BLACK);

        final Intent fillIntent = new Intent();
        fillIntent.setAction(FleekardAppWidget.ACTION_TOAST);
        final Bundle bundle = new Bundle();
        bundle.putString(FleekardAppWidget.EXTRA_STRING, mCollections.get(i).toString());
        fillIntent.putExtras(bundle);
        mView.setOnClickFillInIntent(android.R.id.text1, fillIntent);

        return mView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
