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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.model.Favorite;
import io.github.tiagofrbarbosa.fleekard.model.User;
import io.github.tiagofrbarbosa.fleekard.utils.myUtils;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoritesViewHolder>{

    private List<Favorite> favorites;
    private Context context;
    private final FavoriteAdapter.FavoriteOnclickListener onClickListener;
    private FleekardApplication app;
    private Favorite favorite;
    private String mTime;
    private myUtils mUtils;

    @Inject
    Glide glide;

    public interface FavoriteOnclickListener{
        public void onClickFavorite(FavoriteAdapter.FavoritesViewHolder holder, int idx);
    }

    public FavoriteAdapter(Context context, List<Favorite> favorites, FavoriteOnclickListener onClickListener, FleekardApplication app){
        this.context = context;
        this.favorites = favorites;
        this.onClickListener = onClickListener;
        this.app = app;
        this.mUtils = new myUtils(context);
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.textview_holder_favorite, parent, false);
        FavoritesViewHolder holder = new FavoritesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FavoritesViewHolder holder, final int position) {
        favorite = favorites.get(position);

        mTime = mUtils.longToDate(favorite.getTimeStampLong(), "dd/MM/yy");

        DatabaseReference mUserReference = app.getmFirebaseDatabase().getReference()
                .child(Database.users.CHILD_USERS);

        mUserReference
                .orderByChild(Database.users.USER_KEY)
                .equalTo(favorite.getUserKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot userSnap : dataSnapshot.getChildren()){
                            User user = userSnap.getValue(User.class);

                            if(!user.getImg().equals(Database.users.USER_IMAGE_AVATAR)) {
                                try {
                                    glide.with(context).load(user.getImg())
                                            .apply(RequestOptions.circleCropTransform()).into(holder.imageView);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }else{
                                try {
                                    glide.with(context)
                                            .load(Database.users.USER_AVATAR_IMG)
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(holder.imageView);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }

                            holder.userName.setText(user.getUserName());
                            holder.userFavoriteData.setText(mTime);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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
