package io.github.tiagofrbarbosa.fleekard.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class Notification {

    private String userKey;
    private String userKeyNotificate;
    private int notification;
    private HashMap<String, Object> mTimeStamp;

    public static final int INTERACTION_CODE_MSG = 1;
    public static final int INTERACTION_CODE_LIKE = 2;
    public static final int INTERACTION_CODE_VISIT = 3;

    public Notification(){}

    public Notification(String userKey, String userKeyNotificate, int notification){
        this.userKey = userKey;
        this.userKeyNotificate = userKeyNotificate;
        this.notification = notification;
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

    public void setUserKeyNotificate(String userKeyNotificate){
        this.userKeyNotificate = userKeyNotificate;
    }

    public String getUserKeyNotificate(){
        return this.userKeyNotificate;
    }

    public void setNotification(int notification){
        this.notification = notification;
    }

    public int getNotification(){
        return this.notification;
    }

    public HashMap<String, Object> getmTimeStamp(){
        return this.mTimeStamp;
    }

    @Exclude
    public long getTimeStampLong(){
        return (long) mTimeStamp.get("timestamp");
    }

    public static List<Notification> getNotifications(){
        List<Notification> notifications = new ArrayList<Notification>();
        return notifications;
    }
}
