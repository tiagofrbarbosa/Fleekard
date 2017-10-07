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

    private String userKey;
    private String userFavoriteDate;

    public Favorite(){}

    public Favorite(String userKey, String userFavoriteDate){
        this.userKey = userKey;
        this.userFavoriteDate = userFavoriteDate;
    }

    public void setUserKey(String userKey){
        this.userKey = userKey;
    }

    public String getUserKey(){
        return this.userKey;
    }

    public void setUserFavoriteDate(String userFavoriteDate){
        this.userFavoriteDate = userFavoriteDate;
    }

    public String getUserFavoriteDate(){
        return this.userFavoriteDate;
    }

    public static List<Favorite> getFavorites(){
        List<Favorite> favorites = new ArrayList<Favorite>();
        return favorites;
    }
}