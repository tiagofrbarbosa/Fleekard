package io.github.tiagofrbarbosa.fleekard.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by tfbarbosa on 17/09/17.
 */

public class Message implements Parcelable {

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

    public void setmTimeStamp(HashMap<String, Object> mTimeStamp){
        this.mTimeStamp = mTimeStamp;
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

    protected Message(Parcel in) {
        text = in.readString();
        userId = in.readString();
        name = in.readString();
        photoUrl = in.readString();
        mTimeStamp = (HashMap) in.readValue(HashMap.class.getClassLoader());
        readMessage = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(photoUrl);
        dest.writeValue(mTimeStamp);
        dest.writeByte((byte) (readMessage ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}