package com.example.zyz.database;

/**
 * Created by ZYZ on 2017/11/18.
 */

public class User {
    private String name;
    private String password;

    User(){}

    User(String name, String password)
    {
        this.name = name;
        this.password = password;
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
