package io.github.tiagofrbarbosa.fleekard.model;

import com.google.firebase.database.Exclude;

/**
 * Created by tfbarbosa on 21/09/17.
 */

public class UserLocation {

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
}
