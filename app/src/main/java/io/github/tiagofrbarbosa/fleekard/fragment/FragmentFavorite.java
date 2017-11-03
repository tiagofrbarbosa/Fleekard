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
import android.widget.TextView;

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
import io.github.tiagofrbarbosa.fleekard.adapter.FavoriteAdapter;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.holder.FavoritesViewHolder;
import io.github.tiagofrbarbosa.fleekard.model.Favorite;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class FragmentFavorite extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.myProgressBar) ProgressBar progressBar;
    @BindView(R.id.animation_view_favorite) LottieAnimationView lottieAnimationView;
    @BindView(R.id.no_favorites_text) TextView noFavoritesText;

    protected FavoriteAdapter adapter;
    protected ArrayList<Favorite> favorites;
    protected FleekardApplication app;
    protected DatabaseReference mFavoriteReference;

    private Parcelable parcelable;
    private static final String RECYCLER_LIST_SATE = "recycler_list_state";
    private static final String FAVORITE_PARCELABLE = "favorite_parcelable";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        app = (FleekardApplication) getActivity().getApplication();

            progressBar.setVisibility(View.VISIBLE);

            mFavoriteReference = app.getmFirebaseDatabase().getReference()
                    .child(Database.favorite.CHILD_FAVORITES)
                    .child(app.getmAppUser().getUserKey());

            favorites = Favorite.getFavorites();

            mFavoriteReference
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (favorites != null) favorites.clear();

                            for (DataSnapshot favSnap : dataSnapshot.getChildren()) {
                                Favorite favorite = favSnap.getValue(Favorite.class);
                                favorites.add(favorite);
                            }

                            if(!favorites.isEmpty()){
                                lottieAnimationView.setVisibility(View.GONE);
                                noFavoritesText.setVisibility(View.GONE);
                            }

                            parcelable = recyclerView.getLayoutManager().onSaveInstanceState();
                            recyclerView.setAdapter(adapter = new FavoriteAdapter(getActivity(), favorites, onClickFavorite(), app));
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
        savedInstanceState.putParcelableArrayList(FAVORITE_PARCELABLE, favorites);
    }

    protected FavoriteAdapter.FavoriteOnclickListener onClickFavorite(){

        return new FavoriteAdapter.FavoriteOnclickListener(){

            @Override
            public void onClickFavorite(FavoritesViewHolder holder, int idx) {
                Favorite favorite = favorites.get(idx);
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra(Database.users.USER_KEY, favorite.getUserKey());
                startActivity(intent);
            }
        };
    }
}