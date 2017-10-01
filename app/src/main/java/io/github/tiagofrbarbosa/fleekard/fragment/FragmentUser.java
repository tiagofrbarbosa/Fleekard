package io.github.tiagofrbarbosa.fleekard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.ChildEventListener;
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
import io.github.tiagofrbarbosa.fleekard.adapter.UserAdapter;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.model.User;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class FragmentUser extends Fragment{

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    protected UserAdapter adapter;
    protected List<User> users;
    private DatabaseReference mFirebaseReference;
    private FleekardApplication app;


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

        app = (FleekardApplication) getActivity().getApplication();

        users = User.getUsers();

        mFirebaseReference = app.getmFirebaseDatabase().getReference()
                .child(Database.users.CHILD_USERS);

        mFirebaseReference
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot userSnap : dataSnapshot.getChildren()) {
                            User user = userSnap.getValue(User.class);
                            users.add(user);
                        }
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        recyclerView.setAdapter(adapter = new UserAdapter(getActivity(), users, onClickUser()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        //recyclerView.setAdapter(adapter = new UserAdapter(getActivity(), users, onClickUser()));
    }

    protected UserAdapter.UserOnclickListener onClickUser(){

        return new UserAdapter.UserOnclickListener(){

            @Override
            public void onClickUser(UserAdapter.UsersViewHolder holder, int idx) {
                User u = users.get(idx);

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ProfileActivity.PROFILE_IMAGE_PATH,u.getImg());
                bundle.putString(ProfileActivity.USER_NAME,u.getUserName());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }
}
