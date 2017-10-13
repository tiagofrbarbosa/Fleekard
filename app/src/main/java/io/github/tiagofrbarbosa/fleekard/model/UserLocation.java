package io.github.tiagofrbarbosa.fleekard.model;

/**
 * Created by tfbarbosa on 21/09/17.
 */

public class UserLocation {

    private double latitude;
    private double longitude;

    public UserLocation(){}

    public UserLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public double getLongitude(){
        return this.longitude;
    }
}
