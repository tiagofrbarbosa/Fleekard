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
import io.github.tiagofrbarbosa.fleekard.model.Favorite;
import io.github.tiagofrbarbosa.fleekard.model.Notification;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class FragmentD extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    protected FavoriteAdapter adapter;
    protected List<Favorite> favorites;

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

        favorites = Favorite.getFavorites(getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter = new FavoriteAdapter(getActivity(), favorites, onClickFavorite()));
    }

    protected FavoriteAdapter.FavoriteOnclickListener onClickFavorite(){

        return new FavoriteAdapter.FavoriteOnclickListener(){

            @Override
            public void onClickFavorite(FavoriteAdapter.FavoritesViewHolder holder, int idx) {
                Favorite f = favorites.get(idx);
                Timber.i(String.valueOf(f.userName));
            }
        };
    }
}