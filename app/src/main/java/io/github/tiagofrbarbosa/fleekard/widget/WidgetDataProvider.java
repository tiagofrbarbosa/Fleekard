package io.github.tiagofrbarbosa.fleekard.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.model.Notification;
import io.github.tiagofrbarbosa.fleekard.model.User;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 18/10/17.
 */


@SuppressWarnings("NewApi")
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private List<Notification> mCollections = new ArrayList<Notification>();
    private Context mContext = null;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static final String TAG = "widgetFirebase";

    public WidgetDataProvider(Context context, Intent intent){
        mContext = context;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if(mFirebaseUser != null){
                    Timber.tag(TAG).e("Logado!");
                    initData();
                }else{
                    Timber.tag(TAG).e("Deslogado!");
                    initData();
                }
            }
        };

        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {}

    private void initData(){

        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mFirebaseUser != null) {
            Timber.tag(TAG).e(mFirebaseUser.getDisplayName());

            DatabaseReference mUserReference = FirebaseDatabase.getInstance().getReference()
                    .child(Database.users.CHILD_USERS);

            mUserReference
                    .orderByChild(Database.users.USER_ID)
                    .equalTo(mFirebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                                User mUser = userSnap.getValue(User.class);
                                Timber.tag(TAG).e(mUser.getUserKey());
                                setWidgetNotifications(mUser.getUserKey());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }else{

            if(mCollections != null) mCollections.clear();
            Notification noUser = new Notification();
            noUser.setUserName(mContext.getResources().getString(R.string.widget_no_user));
            mCollections.add(noUser);
        }
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
                            mCollections.add(notification);
                            Timber.tag(TAG).e(notification.getUserName() + " " + String.valueOf(notification.getNotification()));
                        }
                        update(mContext);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onDestroy() {}

    @Override
    public int getCount() {
        return mCollections.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {

        RemoteViews mView = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        if(!mCollections.get(i).getUserName()
                .equals(mContext.getResources().getString(R.string.widget_no_user))) {

            try {

                Bitmap b = Glide.with(mContext)
                        .asBitmap()
                        .load(mCollections.get(i).getUserPhoto())
                        .apply(RequestOptions.circleCropTransform())
                        .submit()
                        .get();

                mView.setViewVisibility(R.id.user_image, View.VISIBLE);
                mView.setImageViewBitmap(R.id.user_image, b);

            } catch (Exception e) {

                mView.setViewVisibility(R.id.user_image, View.VISIBLE);
                mView.setImageViewResource(R.id.user_image, R.drawable.user_avatar);
            }

            String descNotification = "null";

            int idNotification = mCollections.get(i).getNotification();

            if (idNotification == 1) {
                descNotification = mContext.getResources().getString(R.string.notification_desc_msg);
            } else if (idNotification == 2) {
                descNotification = mContext.getResources().getString(R.string.notification_desc_like);
            } else if (idNotification == 3) {
                descNotification = mContext.getResources().getString(R.string.notification_desc_visited);
            }

            long mSystemTime = mCollections.get(i).getTimeStampLong();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
            Date date = new Date(mSystemTime);
            String mTime = simpleDateFormat.format(date);


            mView.setTextViewText(R.id.notification, descNotification);
            mView.setTextViewText(R.id.notification_timestamp, mTime);

        }else{

            mView.setViewVisibility(R.id.user_image, View.INVISIBLE);
            mView.setTextViewText(R.id.notification, "");
            mView.setTextViewText(R.id.notification_timestamp, "");
        }

        mView.setTextViewText(R.id.user_name, mCollections.get(i).getUserName());
        mView.setTextColor(R.id.user_name, Color.BLACK);

        final Intent fillIntent = new Intent();
        fillIntent.setAction(FleekardAppWidget.ACTION_TOAST);
        final Bundle bundle = new Bundle();
        bundle.putString(FleekardAppWidget.EXTRA_STRING, mCollections.get(i).getUserName());
        fillIntent.putExtras(bundle);
        mView.setOnClickFillInIntent(R.id.notification, fillIntent);

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

    public static void update(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (appWidgetManager != null) {
                ComponentName name = new ComponentName(context, FleekardAppWidget.class);
                int [] appWidgetIds = appWidgetManager.getAppWidgetIds(name);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetCollectionList);
        }
    }

}
