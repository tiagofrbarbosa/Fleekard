package io.github.tiagofrbarbosa.fleekard.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class Chat implements Parcelable {

    private String chatId;
    private String userId;
    private String userIdAuth;
    private String chatPushKey;

    public static final int USER_PRESENCE_ONLINE = 1;
    public static final int USER_PRESENCE_OFFLINE = 0;

    public Chat(){}

    public Chat(String chatId, String userId, String userIdAuth, String chatPushKey){
        this.chatId = chatId;
        this.userId = userId;
        this.userIdAuth = userIdAuth;
        this.chatPushKey = chatPushKey;
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

    public void setUserIdAuth(String userIdAuth){
        this.userIdAuth = userIdAuth;
    }

    public String getUserIdAuth(){
        return this.userIdAuth;
    }

    public void setChatPushKey(String chatPushKey){
        this.chatPushKey = chatPushKey;
    }

    public String getChatPushKey(){
        return this.chatPushKey;
    }

    @Exclude
    public static ArrayList<Chat> getChats(){
        ArrayList<Chat> chats = new ArrayList<Chat>();
        return chats;
    }

    protected Chat(Parcel in) {
        chatId = in.readString();
        userId = in.readString();
        userIdAuth = in.readString();
        chatPushKey = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chatId);
        dest.writeString(userId);
        dest.writeString(userIdAuth);
        dest.writeString(chatPushKey);
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
