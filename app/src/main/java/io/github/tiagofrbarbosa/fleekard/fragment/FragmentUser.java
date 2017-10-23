package io.github.tiagofrbarbosa.fleekard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.activity.ProfileActivity;
import io.github.tiagofrbarbosa.fleekard.activity.prefs.SettingsActivity;
import io.github.tiagofrbarbosa.fleekard.adapter.UserAdapter;
import io.github.tiagofrbarbosa.fleekard.asynctask.DistanceAsyncTask;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.model.User;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class FragmentUser extends Fragment{

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.myProgressBar) ProgressBar progressBar;

    protected UserAdapter adapter;
    protected ArrayList<User> users;
    private DatabaseReference mFirebaseReference;
    private FleekardApplication app;
    private FirebaseUser mFirebaseUser;

    private Parcelable parcelable;
    private static final String RECYCLER_LIST_SATE = "recycler_list_state";
    private static final String USER_PARCELABLE = "user_parcelable";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        final int ageRangePref = Integer.valueOf(SettingsActivity.getAgeRange(getActivity()));

        final boolean checkMalePref = SettingsActivity.isCheckMale(getActivity());
        final boolean checkFemalePref = SettingsActivity.isCheckFemale(getActivity());

        final int distancePref = Integer.valueOf(SettingsActivity.getEditDistance(getActivity()));

        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        app = (FleekardApplication) getActivity().getApplication();

        if(savedInstanceState == null) {
            mFirebaseUser = app.getmFirebaseAuth().getCurrentUser();

            users = User.getUsers();

            mFirebaseReference = app.getmFirebaseDatabase().getReference()
                    .child(Database.users.CHILD_USERS);

            mFirebaseReference
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                                User user = userSnap.getValue(User.class);

                                DistanceAsyncTask distanceAsyncTask = new DistanceAsyncTask();
                                distanceAsyncTask.execute(
                                        app.getmAppUser().getUserLocation().getLatLong()
                                        , user.getUserLocation().getLatLong()
                                        , app.getmAppUser().getUserLocation().getLatitude()
                                        , app.getmAppUser().getUserLocation().getLongitude()
                                        , user.getUserLocation().getLatitude()
                                        , user.getUserLocation().getLongitude());

                                try {
                                    user.setDistance(distanceAsyncTask.get());
                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                }

                                String mDistance = user.getDistance();
                                String mDistanceReplaceKM = mDistance.replace(" km", "");
                                String mDistanceReplaceM = mDistanceReplaceKM.replace(" m", "");
                                String mDistanceNumberFormat = mDistanceReplaceM.replace("0.", "");

                                if (Float.valueOf(mDistanceNumberFormat) <= distancePref) {
                                    if (checkMalePref && checkFemalePref) {

                                        if (!mFirebaseUser.getUid().equals(user.getUserId())
                                                && user.getAge() <= ageRangePref) users.add(user);

                                    } else if (!checkMalePref && checkFemalePref) {

                                        if (!mFirebaseUser.getUid().equals(user.getUserId())
                                                && user.getAge() <= ageRangePref
                                                && user.getGender() == User.GENDER_VALUE_FEMALE)
                                            users.add(user);

                                    } else if (checkMalePref && !checkFemalePref) {

                                        if (!mFirebaseUser.getUid().equals(user.getUserId())
                                                && user.getAge() <= ageRangePref
                                                && user.getGender() == User.GENDER_VALUE_MALE)
                                            users.add(user);

                                    } else if (!checkMalePref && !checkFemalePref) {

                                        if (!mFirebaseUser.getUid().equals(user.getUserId())
                                                && user.getAge() <= ageRangePref) users.add(user);
                                    }
                                }

                            }
                            recyclerView.setAdapter(adapter = new UserAdapter(getActivity(), users, onClickUser(), app));
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }else{

            progressBar.setVisibility(View.INVISIBLE);
            users = savedInstanceState.getParcelableArrayList(USER_PARCELABLE);
            recyclerView.setAdapter(adapter = new UserAdapter(getActivity(), users, onClickUser(), app));
            parcelable = savedInstanceState.getParcelable(RECYCLER_LIST_SATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(parcelable);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        parcelable = recyclerView.getLayoutManager().onSaveInstanceState();
        savedInstanceState.putParcelable(RECYCLER_LIST_SATE, parcelable);
        savedInstanceState.putParcelableArrayList(USER_PARCELABLE, users);
    }

    protected UserAdapter.UserOnclickListener onClickUser(){

        return new UserAdapter.UserOnclickListener(){

            @Override
            public void onClickUser(UserAdapter.UsersViewHolder holder, int idx) {
                User u = users.get(idx);

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Database.users.USER_KEY, u.getUserKey());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }
}
