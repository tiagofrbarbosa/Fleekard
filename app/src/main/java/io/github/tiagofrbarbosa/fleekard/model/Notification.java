package io.github.tiagofrbarbosa.fleekard.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class Notification {

    public String img;
    public String notification;

    public Notification(String img, String notification){
        this.img = img;
        this.notification = notification;
    }

    public static List<Notification> getNotifications(){

        List<Notification> notifications = new ArrayList<Notification>();

        for(int i=0;i<50;i++) {
            notifications.add(new Notification("https://api.adorable.io/avatars/285/" + i + ".png", "teste" + i));
        }

        return notifications;
    }
}
