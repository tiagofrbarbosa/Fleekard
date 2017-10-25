package io.github.tiagofrbarbosa.fleekard.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class Message {

    private String text;
    private String userId;
    private String name;
    private String photoUrl;
    private HashMap<String, Object> mTimeStamp;
    private boolean readMessage;

    public Message() {
    }

    public Message(String text, String userId, String name, String photoUrl, boolean readMessage) {
        this.text = text;
        this.userId = userId;
        this.name = name;
        this.photoUrl = photoUrl;
        HashMap<String, Object> stampHash = new HashMap<>();
        stampHash.put("timestamp", ServerValue.TIMESTAMP);
        this.mTimeStamp = stampHash;
        this.readMessage = readMessage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId(){
        return this.userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public HashMap<String, Object> getmTimeStamp(){
        return this.mTimeStamp;
    }

    public void setReadMessage(boolean readMessage){
        this.readMessage = readMessage;
    }

    public boolean isReadMessage(){
        return this.readMessage;
    }

    @Exclude
    public long getTimeStampLong(){
        return (long) mTimeStamp.get("timestamp");
    }
}