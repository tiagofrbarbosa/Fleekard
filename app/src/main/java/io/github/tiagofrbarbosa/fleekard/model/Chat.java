package io.github.tiagofrbarbosa.fleekard.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class Chat {

    public String img;
    public String userName;
    public String userStatus;
    public int userPresence;
    public int chatUnread;

    public Chat(String img, String userName, String userStatus, int userPresence, int chatUnread){
        this.img = img;
        this.userName = userName;
        this.userStatus = userStatus;
        this.userPresence = userPresence;
        this.chatUnread = chatUnread;
    }

    public static List<Chat> getChats(){

        List<Chat> chats = new ArrayList<Chat>();

        for(int i=0;i<50;i++) {
            chats.add(new Chat("https://api.adorable.io/avatars/285/" + i + ".png",
                    "teste" + i, "status" + i, i, i));
        }

        return chats;
    }
}
