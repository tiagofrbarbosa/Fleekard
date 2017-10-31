package io.github.tiagofrbarbosa.fleekard.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.R;

/**
 * Created by tfbarbosa on 30/10/17.
 */

public class NotificationsViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.user_notification) TextView notifications;
    @BindView(R.id.user_image) ImageView imageView;
    @BindView(R.id.user_notification_date) TextView notification_date;
    @BindView(R.id.badge_notification) TextView notification_badge;
    private View view;

    public NotificationsViewHolder(View view) {
        super(view);
        this.view = view;
        ButterKnife.bind(this, view);
    }

    public TextView getUserName() {
        return this.userName;
    }

    public TextView getNotifications() {
        return this.notifications;
    }

    public ImageView getImageView() {
        return this.imageView;
    }

    public TextView getNotification_date() {
        return this.notification_date;
    }

    public TextView getNotification_badge() {
        return this.notification_badge;
    }
}
