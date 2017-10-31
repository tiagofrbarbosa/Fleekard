package io.github.tiagofrbarbosa.fleekard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import javax.inject.Inject;

import io.github.tiagofrbarbosa.fleekard.FleekardApplication;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.firebaseConstants.Database;
import io.github.tiagofrbarbosa.fleekard.holder.FavoritesViewHolder;
import io.github.tiagofrbarbosa.fleekard.model.Favorite;
import io.github.tiagofrbarbosa.fleekard.model.User;
import io.github.tiagofrbarbosa.fleekard.utils.MyUtils;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoritesViewHolder>{

    private List<Favorite> favorites;
    private Context context;
    private final FavoriteAdapter.FavoriteOnclickListener onClickListener;
    private FleekardApplication app;
    private Favorite favorite;
    private String mTime;
    private MyUtils mUtils;

    @Inject
    Glide glide;

    public interface FavoriteOnclickListener{
        void onClickFavorite(FavoritesViewHolder holder, int idx);
    }

    public FavoriteAdapter(Context context, List<Favorite> favorites, FavoriteOnclickListener onClickListener, FleekardApplication app){
        this.context = context;
        this.favorites = favorites;
        this.onClickListener = onClickListener;
        this.app = app;
        this.mUtils = new MyUtils(context);
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
                                            .apply(RequestOptions.circleCropTransform()).into(holder.getImageView());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }else{
                                try {
                                    glide.with(context)
                                            .load(Database.users.USER_AVATAR_IMG)
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(holder.getImageView());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }

                            holder.getUserName().setText(user.getUserName());
                            holder.getUserFavoriteData().setText(mTime);
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
}
