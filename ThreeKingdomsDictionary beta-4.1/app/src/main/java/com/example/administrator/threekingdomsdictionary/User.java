package com.example.administrator.threekingdomsdictionary;

import java.io.Serializable;

/**
 * Created by ZYZ on 2017/11/18.
 */

public class User implements Serializable
{
    private String name;
    private String password;

    User(){}

    public User(String name, String password)
    {
        this.name = name;
        this.password = password;
    }

    public boolean equals(User user)
    {
        if(!this.name.equals(user.getName()))return false;
        else if(!this.password.equals(user.getPassword()))return false;
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
