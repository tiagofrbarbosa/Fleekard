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

    public String img;
    public String userName;
    public String userFavoriteDate;

    public Favorite(String img, String userName, String userFavoriteDate){
        this.img = img;
        this.userName = userName;
        this.userFavoriteDate = userFavoriteDate;
    }

    public static List<Favorite> getFavorites(Context context){

        List<Favorite> favorites = new ArrayList<Favorite>();

        for(int i=0;i<50;i++) {
            favorites.add(new Favorite("https://api.adorable.io/avatars/285/" + i + ".png", "teste" + i,
                    context.getResources().getString(R.string.favorite_user_text) + i));
        }

        return favorites;
    }
}