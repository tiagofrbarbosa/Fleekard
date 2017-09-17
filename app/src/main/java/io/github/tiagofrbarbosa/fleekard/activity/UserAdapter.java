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
    private final UserOnclickListener onClickListener;

    @Inject Glide glide;

    public interface UserOnclickListener{
        public void onClickUser(UsersViewHolder holder, int idx);
    }

    public UserAdapter(Context context, List<User> users, UserOnclickListener onClickListener){
        this.context = context;
        this.users = users;
        this.onClickListener = onClickListener;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_holder_user, parent, false);
        UsersViewHolder holder = new UsersViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final UsersViewHolder holder, final int position) {
        User user = users.get(position);
        holder.userName.setText(user.userName);
        glide.with(context).load(user.img).into(holder.imageView);
        Timber.e(String.valueOf(user.img));

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickUser(holder, position);
                }
            });
        }
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
