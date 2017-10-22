package io.github.tiagofrbarbosa.fleekard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.activity.ProfileActivity;
import io.github.tiagofrbarbosa.fleekard.adapter.NotificationAdapter;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.model.Notification;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class FragmentNotification extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.myProgressBar) ProgressBar progressBar;

    protected NotificationAdapter adapter;
    protected List<Notification> notifications;
    protected FleekardApplication app;
    protected DatabaseReference mNotificationReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

            progressBar.setVisibility(View.VISIBLE);

            app = (FleekardApplication) getActivity().getApplication();

            mNotificationReference = app.getmFirebaseDatabase().getReference()
                    .child(Database.notification.CHILD_NOTIFICATION)
                    .child(app.getmAppUser().getUserKey());

            notifications = Notification.getNotifications();

            mNotificationReference
                    .orderByChild(Database.notification.USER_KEY_NOTIFICATE)
                    .equalTo(app.getmAppUser().getUserKey())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (notifications != null) notifications.clear();

                            for (DataSnapshot notificationSnap : dataSnapshot.getChildren()) {
                                Notification notification = notificationSnap.getValue(Notification.class);
                                notifications.add(notification);
                            }

                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            layoutManager.setReverseLayout(true);
                            layoutManager.setStackFromEnd(true);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter = new NotificationAdapter(getActivity(), notifications, onClickNotification(), app));
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        setRetainInstance(true);
    }

    protected NotificationAdapter.NotificationOnclickListener onClickNotification(){

        return new NotificationAdapter.NotificationOnclickListener(){

            @Override
            public void onClickNotification(NotificationAdapter.NotificationsViewHolder holder, int idx) {
                Notification n = notifications.get(idx);
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra(Database.users.USER_KEY, n.getUserKey());
                startActivity(intent);
            }
        };
    }
}