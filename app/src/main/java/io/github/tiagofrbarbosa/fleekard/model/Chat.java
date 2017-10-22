package io.github.tiagofrbarbosa.fleekard.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class Chat implements Parcelable {

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
    public static ArrayList<Chat> getChats(){
        ArrayList<Chat> chats = new ArrayList<Chat>();
        return chats;
    }

    protected Chat(Parcel in) {
        chatId = in.readString();
        userId = in.readString();
        userPresence = in.readInt();
        msgUnread = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chatId);
        dest.writeString(userId);
        dest.writeInt(userPresence);
        dest.writeInt(msgUnread);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Chat> CREATOR = new Parcelable.Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };
}
