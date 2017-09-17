package io.github.tiagofrbarbosa.fleekard.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.model.Notification;
import io.github.tiagofrbarbosa.fleekard.model.User;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class FragmentB extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    protected NotificationAdapter adapter;
    protected List<Notification> notifications;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        notifications = Notification.getNotifications();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter = new NotificationAdapter(getActivity(), notifications, onClickNotification()));
    }

    protected NotificationAdapter.NotificationOnclickListener onClickNotification(){

        return new NotificationAdapter.NotificationOnclickListener(){

            @Override
            public void onClickNotification(NotificationAdapter.NotificationsViewHolder holder, int idx) {
                Notification n = notifications.get(idx);
                Timber.i(String.valueOf(n.notification));
            }
        };
    }
}