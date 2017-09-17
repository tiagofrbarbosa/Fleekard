package io.github.tiagofrbarbosa.fleekard.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.model.User;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UsersViewHolder>{

    private List<User> users;
    private Context context;

    @Inject Glide glide;

    public UserAdapter(Context context, List<User> users){
        this.context = context;
        this.users = users;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_holder_user, parent, false);
        UsersViewHolder holder = new UsersViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, int position) {
        User user = users.get(position);
        holder.userName.setText(user.userName);
        glide.with(context).load(user.img).into(holder.imageView);
        Timber.e(String.valueOf(user.img));
    }

    @Override
    public int getItemCount() {
        return this.users != null ? this.users.size() : 0;
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.user_name) TextView userName;
        @BindView(R.id.user_image) ImageView imageView;
        private View view;

        public UsersViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }
}
