package io.github.tiagofrbarbosa.fleekard.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class Chat {

    private User user;
    private int userPresence;
    private int msgUnread;

    public static final int USER_PRESENCE_ONLINE = 1;
    public static final int USER_PRESENCE_OFFLINE = 0;

    public Chat(){}

    public Chat(User user, int userPresence, int msgUnread){
        this.user = user;
        this.userPresence = userPresence;
        this.msgUnread = msgUnread;
    }

    public User getUser(){
        return this.user;
    }

    public int getUserPresence(){
        return this.userPresence;
    }

    public int getMsgUnread(){
        return this.msgUnread;
    }

    public static List<Chat> getChats(){

        List<Chat> chats = new ArrayList<Chat>();

        for(int i=0;i<50;i++) {
            User user = new User();
            user.setImg("https://api.adorable.io/avatars/285/" + i + ".png");
            user.setUserName("User " + i + " ");
            user.setUserStatus("Status " + i + " ");
            chats.add(new Chat(user, 1, i));
        }

        return chats;
    }
}
