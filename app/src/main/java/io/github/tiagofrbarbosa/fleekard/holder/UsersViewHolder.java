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

public class UsersViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.user_image) ImageView imageView;
    @BindView(R.id.user_distance) TextView userDistance;
    @BindView(R.id.user_age) TextView userAge;
    @BindView(R.id.user_gender) ImageView userGender;
    private View view;

    public UsersViewHolder(View view) {
        super(view);
        this.view = view;
        ButterKnife.bind(this, view);
    }

    public TextView getUserName(){
        return this.userName;
    }

    public ImageView getImageView(){
        return this.imageView;
    }

    public TextView getUserDistance(){
        return this.userDistance;
    }

    public TextView getUserAge(){
        return this.userAge;
    }

    public ImageView getUserGender(){
        return this.userGender;
    }
}
