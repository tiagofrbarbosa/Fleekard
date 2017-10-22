package io.github.tiagofrbarbosa.fleekard.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by tfbarbosa on 21/09/17.
 */

public class UserLocation implements Parcelable {

    private String latitude;
    private String longitude;

    public UserLocation(){}

    public UserLocation(String latitude, String longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLatitude(String latitude){
        this.latitude = latitude;
    }

    public String getLatitude(){
        return this.latitude;
    }

    public void setLongitude(String longitude){
        this.longitude = longitude;
    }

    public String getLongitude(){
        return this.longitude;
    }

    @Exclude
    public String getLatLong(){
        return this.latitude + "," + this.longitude;
    }

    protected UserLocation(Parcel in) {
        latitude = in.readString();
        longitude = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(latitude);
        dest.writeString(longitude);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserLocation> CREATOR = new Parcelable.Creator<UserLocation>() {
        @Override
        public UserLocation createFromParcel(Parcel in) {
            return new UserLocation(in);
        }

        @Override
        public UserLocation[] newArray(int size) {
            return new UserLocation[size];
        }
    };
}
