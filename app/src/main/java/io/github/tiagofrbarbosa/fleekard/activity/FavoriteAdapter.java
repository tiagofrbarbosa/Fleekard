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
import io.github.tiagofrbarbosa.fleekard.model.Favorite;
import io.github.tiagofrbarbosa.fleekard.model.Notification;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoritesViewHolder>{

    private List<Favorite> favorites;
    private Context context;
    private final FavoriteAdapter.FavoriteOnclickListener onClickListener;

    @Inject
    Glide glide;

    public interface FavoriteOnclickListener{
        public void onClickFavorite(FavoriteAdapter.FavoritesViewHolder holder, int idx);
    }

    public FavoriteAdapter(Context context, List<Favorite> favorites, FavoriteOnclickListener onClickListener){
        this.context = context;
        this.favorites = favorites;
        this.onClickListener = onClickListener;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.textview_holder_favorite, parent, false);
        FavoritesViewHolder holder = new FavoritesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FavoritesViewHolder holder, final int position) {
        Favorite favorite = favorites.get(position);
        glide.with(context).load(favorite.img).apply(RequestOptions.circleCropTransform()).into(holder.imageView);
        holder.userName.setText(favorite.userName);
        holder.userFavoriteData.setText(favorite.userFavoriteDate);
        Timber.e(String.valueOf(favorite.img));

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickFavorite(holder, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.favorites != null ? this.favorites.size() : 0;
    }

    public static class FavoritesViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.user_image) ImageView imageView;
        @BindView(R.id.user_name) TextView userName;
        @BindView(R.id.user_favorite_date) TextView userFavoriteData;
        private View view;

        public FavoritesViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }
}
