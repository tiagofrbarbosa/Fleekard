package io.github.tiagofrbarbosa.fleekard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.model.Notification;
import io.github.tiagofrbarbosa.fleekard.model.User;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationsViewHolder>{

    private List<Notification> notifications;
    private Context context;
    private final NotificationAdapter.NotificationOnclickListener onClickListener;
    private FleekardApplication app;
    private String descNotification;
    private Notification notification;
    private String mTime;

    @Inject Glide glide;

    public interface NotificationOnclickListener{
        public void onClickNotification(NotificationAdapter.NotificationsViewHolder holder, int idx);
    }

    public NotificationAdapter(Context context, List<Notification> notifications, NotificationOnclickListener onClickListener, FleekardApplication app){
        this.context = context;
        this.notifications = notifications;
        this.onClickListener = onClickListener;
        this.app = app;
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

        long mSystemTime = notification.getTimeStampLong();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        Date date = new Date(mSystemTime);
        mTime = simpleDateFormat.format(date);

        holder.notification_date.setText(mTime);

        if(notification.getNotification() == Notification.INTERACTION_CODE_MSG){
            descNotification = context.getResources().getString(R.string.notification_desc_msg);
        }
        if(notification.getNotification() == Notification.INTERACTION_CODE_LIKE){
            descNotification = context.getResources().getString(R.string.notification_desc_like);
        }
        if(notification.getNotification() == Notification.INTERACTION_CODE_VISIT){
            descNotification = context.getResources().getString(R.string.notification_desc_visited);
        }

        holder.notifications.setText(descNotification);

        DatabaseReference mUserReference = app.getmFirebaseDatabase().getReference()
                .child(Database.users.CHILD_USERS);

        mUserReference
                .orderByChild(Database.users.USER_KEY)
                .equalTo(notification.getUserKey())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot userSnap : dataSnapshot.getChildren()){
                            User user = userSnap.getValue(User.class);

                            if(!user.getImg().equals(Database.users.USER_IMAGE_AVATAR)) {

                                glide.with(context).load(user.getImg())
                                        .apply(RequestOptions.circleCropTransform()).into(holder.imageView);
                            }else{

                                glide.with(context)
                                        .load(Database.users.USER_AVATAR_IMG)
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(holder.imageView);
                            }

                            holder.userName.setText(user.getUserName());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickNotification(holder, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.notifications != null ? this.notifications.size() : 0;
    }

    public static class NotificationsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.user_name) TextView userName;
        @BindView(R.id.user_notification) TextView notifications;
        @BindView(R.id.user_image) ImageView imageView;
        @BindView(R.id.user_notification_date) TextView notification_date;
        private View view;

        public NotificationsViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }
}