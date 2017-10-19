package io.github.tiagofrbarbosa.fleekard.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.model.Notification;
import io.github.tiagofrbarbosa.fleekard.model.User;
import timber.log.Timber;

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

        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Timber.tag("widgetFirebase").e(mFirebaseUser.getDisplayName());

        DatabaseReference mUserReference = FirebaseDatabase.getInstance().getReference()
                .child(Database.users.CHILD_USERS);

        mUserReference
                .orderByChild(Database.users.USER_ID)
                .equalTo(mFirebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot userSnap : dataSnapshot.getChildren()){
                            User mUser = userSnap.getValue(User.class);
                            Timber.tag("widgetFirebase").e(mUser.getUserKey());
                            setWidgetNotifications(mUser.getUserKey());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void setWidgetNotifications(String userKey){
        DatabaseReference mNotificationReference = FirebaseDatabase.getInstance().getReference()
                .child(Database.notification.CHILD_NOTIFICATION)
                .child(userKey);

        mNotificationReference
                .orderByChild(Database.notification.USER_KEY_NOTIFICATE)
                .equalTo(userKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(mCollections != null) mCollections.clear();

                        for(DataSnapshot notificationSnap : dataSnapshot.getChildren()){
                            Notification notification = notificationSnap.getValue(Notification.class);
                            mCollections.add(String.valueOf(notification.getTimeStampLong()));
                            Timber.tag("widgetFirebase").e(String.valueOf(notification.getTimeStampLong()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
