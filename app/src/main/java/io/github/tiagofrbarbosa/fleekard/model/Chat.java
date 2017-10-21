package io.github.tiagofrbarbosa.fleekard.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class Chat {

    private String chatId;
    private String userId;
    private int userPresence;
    private int msgUnread;

    public static final int USER_PRESENCE_ONLINE = 1;
    public static final int USER_PRESENCE_OFFLINE = 0;

    public Chat(){}

    public Chat(String chatId, String userId, int userPresence, int msgUnread){
        this.chatId = chatId;
        this.userId = userId;
        this.userPresence = userPresence;
        this.msgUnread = msgUnread;
    }

    public void setChatId(String chatId){
        this.chatId = chatId;
    }

    public String getChatId(){
        return this.chatId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return this.userId;
    }

    public void setUserPresence(int userPresence){
        this.userPresence = userPresence;
    }

    public int getUserPresence(){
        return this.userPresence;
    }

    public void setMsgUnread(int msgUnread){
        this.msgUnread = msgUnread;
    }

    public int getMsgUnread(){
        return this.msgUnread;
    }

    @Exclude
    public static List<Chat> getChats(){
        List<Chat> chats = new ArrayList<Chat>();
        return chats;
    }
}
