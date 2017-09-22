package io.github.tiagofrbarbosa.fleekard.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class Notification {

    private User user;
    private int notification;

    public static final int INTERACTION_CODE_MSG = 1;
    public static final int INTERACTION_CODE_LIKE = 2;
    public static final int INTERACTION_CODE_VISIT = 3;

    public Notification(){}

    public Notification(User user, int notification){
        this.user = user;
        this.notification = notification;
    }

    public User getUser(){
        return this.user;
    }

    public int getNotification(){
        return this.notification;
    }

    public static List<Notification> getNotifications(){

        List<Notification> notifications = new ArrayList<Notification>();

        for(int i=0;i<50;i++) {
            User user = new User();
            user.setUserName("User " + i + " ");
            user.setImg("https://api.adorable.io/avatars/285/" + i + ".png");
            notifications.add(new Notification(user, 1));
        }

        return notifications;
    }
}
