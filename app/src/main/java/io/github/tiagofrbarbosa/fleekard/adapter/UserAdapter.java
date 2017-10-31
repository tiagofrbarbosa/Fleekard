package io.github.tiagofrbarbosa.fleekard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import javax.inject.Inject;

import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.holder.UsersViewHolder;
import io.github.tiagofrbarbosa.fleekard.model.User;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UsersViewHolder>{

    private List<User> users;
    private Context context;
    private final UserOnclickListener onClickListener;
    private FleekardApplication app;

    @Inject Glide glide;

    public interface UserOnclickListener{
        public void onClickUser(UsersViewHolder holder, int idx);
    }

    public UserAdapter(Context context, List<User> users, UserOnclickListener onClickListener, FleekardApplication app){
        this.context = context;
        this.users = users;
        this.onClickListener = onClickListener;
        this.app = app;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_holder_user, parent, false);
        UsersViewHolder holder = new UsersViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final UsersViewHolder holder, final int position) {
        if(users.size() >= 0) {
            User user = users.get(position);
            holder.getUserName().setText(user.getUserName());

            try {
                glide.with(context)
                        .load(user.getImg())
                        .apply(RequestOptions.placeholderOf(R.drawable.user_avatar))
                        .into(holder.getImageView());
            }catch (Exception e){
                e.printStackTrace();
            }

            holder.getUserDistance().setText(user.getDistance());
            holder.getUserAge().setText(String.valueOf(user.getAge()));

            if (user.getGender() == 0) {
                holder.getUserGender().setImageResource(R.drawable.ic_male);
            } else {
                holder.getUserGender().setImageResource(R.drawable.ic_female);
            }

            if (onClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClickUser(holder, position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.users != null ? this.users.size() : 0;
    }
}
