package io.github.tiagofrbarbosa.fleekard.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.tiagofrbarbosa.fleekard.R;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class User {
    private String userId;
    private String userKey;
    private String userName;
    private String userStatus;
    private String img;
    private String email;
    private int gender;
    private int age;
    private int userPresence;
    private Location location;

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

    public User(String userId, String userKey, String userName, String userStatus, String img, String email, int gender, int age, int userPresence){
        this.userId = userId;
        this.userKey = userKey;
        this.userName = userName;
        this.userStatus = userStatus;
        this.img = img;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.userPresence = userPresence;
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

    public void setLocation(Location location){
        this.location = location;
    }

    public Location getLocation(){
        return this.location;
    }

    public static List<User> getUsers(){
        List<User> users = new ArrayList<User>();
        return users;
    }
}
