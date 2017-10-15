package io.github.tiagofrbarbosa.fleekard.adapter;

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
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.asynctask.DistanceAsyncTask;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.model.User;
import io.github.tiagofrbarbosa.fleekard.retrofit.RetrofitClient;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UsersViewHolder>{

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
        User user = users.get(position);
        holder.userName.setText(user.getUserName());

        Timber.tag("mAsyncTask").i("user connected: " + app.getmAppUser().getUserLocation().getLatLong());
        Timber.tag("mAsyncTask").i("user holder: " + user.getUserLocation().getLatLong());

        DistanceAsyncTask distanceAsyncTask = new DistanceAsyncTask();
        distanceAsyncTask.execute(
                  app.getmAppUser().getUserLocation().getLatLong()
                , user.getUserLocation().getLatLong()
                , app.getmAppUser().getUserLocation().getLatitude()
                , app.getmAppUser().getUserLocation().getLongitude()
                , user.getUserLocation().getLatitude()
                , user.getUserLocation().getLongitude());

        try {
            holder.userDistance.setText(distanceAsyncTask.get());
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
            glide.with(context)
                    .load(user.getImg())
                    .apply(RequestOptions.placeholderOf(R.drawable.user_avatar))
                    .into(holder.imageView);

        holder.userAge.setText(String.valueOf(user.getAge()));

        if(user.getGender() == 0){
            holder.userGender.setImageResource(R.drawable.ic_male);
        }else{
            holder.userGender.setImageResource(R.drawable.ic_female);
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

    @Override
    public int getItemCount() {
        return this.users != null ? this.users.size() : 0;
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
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
    }
}
