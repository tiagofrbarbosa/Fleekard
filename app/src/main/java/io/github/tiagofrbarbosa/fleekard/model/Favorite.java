package io.github.tiagofrbarbosa.fleekard.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.activity.MainActivity;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class Favorite {

    private User user;
    public String userFavoriteDate;

    public Favorite(){}

    public Favorite(User user, String userFavoriteDate){
        this.user = user;
        this.userFavoriteDate = userFavoriteDate;
    }

    public User getUser(){
        return this.user;
    }

    public String getUserFavoriteDate(){
        return this.userFavoriteDate;
    }

    public static List<Favorite> getFavorites(Context context){

        List<Favorite> favorites = new ArrayList<Favorite>();

        for(int i=0;i<50;i++) {
            User user = new User();
            user.setUserName("User " + i + " ");
            user.setImg("https://api.adorable.io/avatars/285/" + i + ".png");
            favorites.add(new Favorite(user, context.getResources().getString(R.string.favorite_user_text) + i));
        }

        return favorites;
    }
}