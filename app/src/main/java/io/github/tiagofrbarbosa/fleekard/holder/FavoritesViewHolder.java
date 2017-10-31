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

public class FavoritesViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.user_image) ImageView imageView;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.user_favorite_date) TextView userFavoriteData;
    private View view;

    public FavoritesViewHolder(View view) {
        super(view);
        this.view = view;
        ButterKnife.bind(this, view);
    }

    public ImageView getImageView() {
        return this.imageView;
    }

    public TextView getUserName() {
        return this.userName;
    }

    public TextView getUserFavoriteData() {
        return this.userFavoriteData;
    }
}
