package io.github.tiagofrbarbosa.fleekard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import javax.inject.Inject;

import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.holder.NotificationsViewHolder;
import io.github.tiagofrbarbosa.fleekard.model.Notification;
import io.github.tiagofrbarbosa.fleekard.model.User;
import io.github.tiagofrbarbosa.fleekard.utils.MyUtils;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationsViewHolder>{

    private List<Notification> notifications;
    private Context context;
    private final NotificationAdapter.NotificationOnclickListener onClickListener;
    private FleekardApplication app;
    private String descNotification;
    private Notification notification;
    private String mTime;
    private MyUtils mUtils;
    private DatabaseReference mNotificationReference;

    @Inject Glide glide;

    public interface NotificationOnclickListener{
        public void onClickNotification(NotificationsViewHolder holder, int idx, DatabaseReference databaseReference);
    }

    public NotificationAdapter(Context context, List<Notification> notifications, NotificationOnclickListener onClickListener, FleekardApplication app){
        this.context = context;
        this.notifications = notifications;
        this.onClickListener = onClickListener;
        this.app = app;
        this.mUtils = new MyUtils(context);
        this.mNotificationReference = app.getmFirebaseDatabase().getReference().child(Database.notification.CHILD_NOTIFICATION);
    }

    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.textview_holder_notification, parent, false);
        NotificationsViewHolder holder = new NotificationsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NotificationsViewHolder holder, final int position) {
        notification = notifications.get(position);

            if(onClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClickNotification(holder, position, null);
                    }
                });
            }

            holder.getNotification_badge().setVisibility(View.INVISIBLE);
            mTime = mUtils.longToDate(notification.getTimeStampLong(), "dd/MM/yy");
            holder.getNotification_date().setText(mTime);

            if (notification.getNotification() == Notification.INTERACTION_CODE_MSG) {
                descNotification = context.getResources().getString(R.string.notification_desc_msg);
            }
            if (notification.getNotification() == Notification.INTERACTION_CODE_LIKE) {
                descNotification = context.getResources().getString(R.string.notification_desc_like);
            }
            if (notification.getNotification() == Notification.INTERACTION_CODE_VISIT) {
                descNotification = context.getResources().getString(R.string.notification_desc_visited);
            }

            holder.getNotifications().setText(descNotification);

            DatabaseReference mUserReference = app.getmFirebaseDatabase().getReference()
                    .child(Database.users.CHILD_USERS);

            mUserReference
                    .orderByChild(Database.users.USER_KEY)
                    .equalTo(notification.getUserKey())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                                User user = userSnap.getValue(User.class);

                                if (!user.getImg().equals(Database.users.USER_IMAGE_AVATAR)) {

                                    try {
                                        glide.with(context).load(user.getImg())
                                                .apply(RequestOptions.circleCropTransform()).into(holder.getImageView());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    try {
                                        glide.with(context)
                                                .load(Database.users.USER_AVATAR_IMG)
                                                .apply(RequestOptions.circleCropTransform())
                                                .into(holder.getImageView());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                holder.getUserName().setText(user.getUserName());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            mNotificationReference
                    .child(notification.getUserKeyNotificate())
                    .child(notification.getNotificationKey())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            Notification notification = dataSnapshot.getValue(Notification.class);

                            if(!notification.isNotificationRead()){
                                holder.getNotification_badge().setVisibility(View.VISIBLE);
                                holder.getNotification_badge().setText(" ! ");

                                    if(onClickListener != null) {
                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                onClickListener.onClickNotification(holder, position, dataSnapshot.getRef());
                                            }
                                        });
                                    }

                            }else{
                                holder.getNotification_badge().setVisibility(View.INVISIBLE);

                                    if(onClickListener != null) {
                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                onClickListener.onClickNotification(holder, position, null);
                                            }
                                        });
                                    }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
    }

    @Override
    public int getItemCount() {
        return this.notifications != null ? this.notifications.size() : 0;
    }
}