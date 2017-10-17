package io.github.tiagofrbarbosa.fleekard.model;

/**
 * Created by tfbarbosa on 17/10/2017.
 */

public class NotificationToken {

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
}
