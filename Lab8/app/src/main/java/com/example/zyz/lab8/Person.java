package com.example.zyz.lab8;

/**
 * Created by ZYZ on 2017/12/5.
 */

public class Person {

    private String name;
    private String birth;
    private String gift;

    public Person(String name, String birth, String gift)
    {
        this.name = name;
        this.birth = birth;
        this.gift = gift;
    }

    public String getName() {
        return name;
    }

    public String getBirth() {
        return birth;
    }

    public String getGift() {
        return gift;
    }
}
