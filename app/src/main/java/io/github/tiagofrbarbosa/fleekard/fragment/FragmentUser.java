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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.activity.ProfileActivity;
import io.github.tiagofrbarbosa.fleekard.adapter.UserAdapter;
import io.github.tiagofrbarbosa.fleekard.model.User;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class FragmentUser extends Fragment{

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    protected UserAdapter adapter;
    protected List<User> users;


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

         users = User.getUsers();

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter = new UserAdapter(getActivity(), users, onClickUser()));
    }

    protected UserAdapter.UserOnclickListener onClickUser(){

        return new UserAdapter.UserOnclickListener(){

            @Override
            public void onClickUser(UserAdapter.UsersViewHolder holder, int idx) {
                User u = users.get(idx);
                Timber.e(String.valueOf(u.userName));

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ProfileActivity.PROFILE_IMAGE_PATH,u.img);
                bundle.putString(ProfileActivity.USER_NAME,u.userName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }
}
