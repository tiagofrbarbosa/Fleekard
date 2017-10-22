package io.github.tiagofrbarbosa.fleekard.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.activity.MainActivity;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class Favorite implements Parcelable {

    private String userKey;
    private HashMap<String, Object> mTimeStamp;

    public Favorite(){}

    public Favorite(String userKey){
        this.userKey = userKey;
        HashMap<String, Object> stampHash = new HashMap<>();
        stampHash.put("timestamp", ServerValue.TIMESTAMP);
        this.mTimeStamp = stampHash;
    }

    public void setUserKey(String userKey){
        this.userKey = userKey;
    }

    public String getUserKey(){
        return this.userKey;
    }

    public HashMap<String, Object> getmTimeStamp(){
        return this.mTimeStamp;
    }

    @Exclude
    public long getTimeStampLong(){
        return (long) mTimeStamp.get("timestamp");
    }

    @Exclude
    public static ArrayList<Favorite> getFavorites(){
        ArrayList<Favorite> favorites = new ArrayList<Favorite>();
        return favorites;
    }

    protected Favorite(Parcel in) {
        userKey = in.readString();
        mTimeStamp = (HashMap) in.readValue(HashMap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userKey);
        dest.writeValue(mTimeStamp);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Favorite> CREATOR = new Parcelable.Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };
}