package io.github.tiagofrbarbosa.fleekard.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class User implements Parcelable {
    private String userId;
    private String userKey;
    private String userName;
    private String userStatus;
    private String img;
    private String email;
    private int gender;
    private int age;
    private int userPresence;
    private UserLocation userLocation;
    private NotificationToken notificationToken;
    private String distance;

    public static final int GENDER_VALUE_MALE = 0;
    public static final int GENDER_VALUE_FEMALE = 1;

    public static final int USER_DISCONNECTED = 0;
    public static final int USER_CONNECTED = 1;

    public User(){};

    //holder test
    public User(String userName, String img){
        this.userName = userName;
        this.img = img;
    }

    public User(String userId, String userName, String userStatus, String email){
        this.userId = userId;
        this.userName = userName;
        this.userStatus = userStatus;
        this.email = email;
    }

    public User(String userName, String userStatus, int gender, int age){
        this.userName = userName;
        this.userStatus = userStatus;
        this.gender = gender;
        this.age = age;
    }

    public User(String userId, String userKey, String userName, String userStatus, String img, String email, int gender, int age, int userPresence, UserLocation userLocation){
        this.userId = userId;
        this.userKey = userKey;
        this.userName = userName;
        this.userStatus = userStatus;
        this.img = img;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.userPresence = userPresence;
        this.userLocation = userLocation;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return this.userId;
    }

    public void setUserKey(String userKey){
        this.userKey = userKey;
    }

    public String getUserKey(){
        return this.userKey;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return this.userName;
    }

    public void setUserStatus(String userStatus){
        this.userStatus = userStatus;
    }

    public String getUserStatus(){
        return this.userStatus;
    }

    public void setImg(String img){
        this.img = img;
    }

    public String getImg(){
        return this.img;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }

    public void setGender(int gender){
        this.gender = gender;
    }

    public int getGender(){
        return this.gender;
    }

    public void setAge(int age){
        this.age = age;
    }

    public int getAge(){
        return this.age;
    }

    public void setUserPresence(int userPresence){
        this.userPresence = userPresence;
    }

    public int getUserPresence(){
        return this.userPresence;
    }

    public void setUserLocation(UserLocation userLocation){
        this.userLocation = userLocation;
    }

    public NotificationToken getNotificationToken(){
        return this.notificationToken;
    }

    public void setNotificationToken(NotificationToken notificationToken){
        this.notificationToken = notificationToken;
    }

    public UserLocation getUserLocation(){
        return this.userLocation;
    }

    @Exclude
    public void setDistance(String distance){
        this.distance = distance;
    }

    @Exclude
    public String getDistance(){
        return this.distance;
    }

    @Exclude
    public static ArrayList<User> getUsers(){
        ArrayList<User> users = new ArrayList<User>();
        return users;
    }

    protected User(Parcel in) {
        userId = in.readString();
        userKey = in.readString();
        userName = in.readString();
        userStatus = in.readString();
        img = in.readString();
        email = in.readString();
        gender = in.readInt();
        age = in.readInt();
        userPresence = in.readInt();
        userLocation = (UserLocation) in.readValue(UserLocation.class.getClassLoader());
        notificationToken = (NotificationToken) in.readValue(NotificationToken.class.getClassLoader());
        distance = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userKey);
        dest.writeString(userName);
        dest.writeString(userStatus);
        dest.writeString(img);
        dest.writeString(email);
        dest.writeInt(gender);
        dest.writeInt(age);
        dest.writeInt(userPresence);
        dest.writeValue(userLocation);
        dest.writeValue(notificationToken);
        dest.writeString(distance);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
