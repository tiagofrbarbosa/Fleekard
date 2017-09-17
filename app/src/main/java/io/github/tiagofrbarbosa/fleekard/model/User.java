package io.github.tiagofrbarbosa.fleekard.model;

import java.util.ArrayList;
import java.util.List;

import io.github.tiagofrbarbosa.fleekard.R;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class User {
    public String userName;
    public String img;

    public User(String userName, String img){
        this.userName = userName;
        this.img = img;
    }

    public static List<User> getUsers(){

        List<User> users = new ArrayList<User>();

        for(int i=0;i<50;i++) {
            users.add(new User("teste" + i, "https://api.adorable.io/avatars/285/" + i + ".png"));
        }

        return users;
    }
}
