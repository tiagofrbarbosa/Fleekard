package io.github.tiagofrbarbosa.fleekard.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.model.Notification;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationsViewHolder>{

    private List<Notification> notifications;
    private Context context;
    private final NotificationAdapter.NotificationOnclickListener onClickListener;

    @Inject Glide glide;

    public interface NotificationOnclickListener{
        public void onClickNotification(NotificationAdapter.NotificationsViewHolder holder, int idx);
    }

    public NotificationAdapter(Context context, List<Notification> notifications, NotificationOnclickListener onClickListener){
        this.context = context;
        this.notifications = notifications;
        this.onClickListener = onClickListener;
    }

    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.textview_holder_notification, parent, false);
        NotificationsViewHolder holder = new NotificationsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NotificationsViewHolder holder, final int position) {
        Notification notification = notifications.get(position);
        glide.with(context).load(notification.img).apply(RequestOptions.circleCropTransform()).into(holder.imageView);
        holder.notifications.setText(notification.notification);
        Timber.e(String.valueOf(notification.img));

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
        @BindView(R.id.user_notification)
        TextView notifications;
        @BindView(R.id.user_image)
        ImageView imageView;
        private View view;

        public NotificationsViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }
}