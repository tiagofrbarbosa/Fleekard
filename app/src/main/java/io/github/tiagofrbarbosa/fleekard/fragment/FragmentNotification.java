package io.github.tiagofrbarbosa.fleekard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.activity.ProfileActivity;
import io.github.tiagofrbarbosa.fleekard.adapter.NotificationAdapter;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.holder.NotificationsViewHolder;
import io.github.tiagofrbarbosa.fleekard.model.Notification;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class FragmentNotification extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.myProgressBar) ProgressBar progressBar;
    @BindView(R.id.animation_view_notification) LottieAnimationView lottieAnimationView;

    protected NotificationAdapter adapter;
    protected ArrayList<Notification> notifications;
    protected FleekardApplication app;
    protected DatabaseReference mNotificationReference;

    private Parcelable parcelable;
    private static final String RECYCLER_LIST_SATE = "recycler_list_state";
    private static final String NOTIFICATION_PARCELABLE = "notification_parcelable";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        app = (FleekardApplication) getActivity().getApplication();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

            mNotificationReference = app.getmFirebaseDatabase().getReference()
                    .child(Database.notification.CHILD_NOTIFICATION)
                    .child(app.getmAppUser().getUserKey());

            notifications = Notification.getNotifications();

            progressBar.setVisibility(View.VISIBLE);

            mNotificationReference
                    .orderByChild(Database.notification.USER_KEY_NOTIFICATE)
                    .equalTo(app.getmAppUser().getUserKey())
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                                if (notifications != null) notifications.clear();

                                for (DataSnapshot notificationSnap : dataSnapshot.getChildren()) {
                                    Notification notification = notificationSnap.getValue(Notification.class);
                                    notification.setNotificationKey(notificationSnap.getKey());
                                    notifications.add(notification);
                                }

                                if(!notifications.isEmpty()) lottieAnimationView.setVisibility(View.GONE);

                                parcelable = recyclerView.getLayoutManager().onSaveInstanceState();
                                recyclerView.setAdapter(adapter = new NotificationAdapter(getActivity(), notifications, onClickNotification(), app));
                                progressBar.setVisibility(View.INVISIBLE);
                                recyclerView.getLayoutManager().onRestoreInstanceState(parcelable);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                                progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        parcelable = recyclerView.getLayoutManager().onSaveInstanceState();
        savedInstanceState.putParcelable(RECYCLER_LIST_SATE, parcelable);
        savedInstanceState.putParcelableArrayList(NOTIFICATION_PARCELABLE, notifications);
    }

    protected NotificationAdapter.NotificationOnclickListener onClickNotification(){

        return new NotificationAdapter.NotificationOnclickListener(){

            @Override
            public void onClickNotification(NotificationsViewHolder holder, int idx, DatabaseReference databaseReference) {
                Notification n = notifications.get(idx);

                if(databaseReference != null) {

                    if (!n.isNotificationRead())
                        databaseReference.child(Database.notification.NOTIFICATION_UNREAD).setValue(true);

                }

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra(Database.users.USER_KEY, n.getUserKey());
                startActivity(intent);
            }
        };
    }
}