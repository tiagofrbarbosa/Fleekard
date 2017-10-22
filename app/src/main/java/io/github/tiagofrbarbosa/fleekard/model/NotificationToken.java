package io.github.tiagofrbarbosa.fleekard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tfbarbosa on 17/10/2017.
 */

public class NotificationToken implements Parcelable {

    private String token;

    public NotificationToken(){}

    public NotificationToken(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }

    public void setToken(String token){
        this.token = token;
    }

    protected NotificationToken(Parcel in) {
        token = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NotificationToken> CREATOR = new Parcelable.Creator<NotificationToken>() {
        @Override
        public NotificationToken createFromParcel(Parcel in) {
            return new NotificationToken(in);
        }

        @Override
        public NotificationToken[] newArray(int size) {
            return new NotificationToken[size];
        }
    };
}
