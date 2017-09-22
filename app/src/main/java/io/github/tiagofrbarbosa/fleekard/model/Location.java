package io.github.tiagofrbarbosa.fleekard.model;

/**
 * Created by tfbarbosa on 21/09/17.
 */

public class Location {

    private String latitude;
    private String longitude;

    public Location(){}

    public Location(String latitude, String longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
