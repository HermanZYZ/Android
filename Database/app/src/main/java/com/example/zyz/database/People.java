package com.example.zyz.database;

/**
 * Created by ZYZ on 2017/11/15.
 */

public class People {
    private int _id;
    private String name;
    private String gender;
    private String subname;//字
    private String bornPlace;//籍贯
    private String bornDate;//生
    private String deadDate;
    private String info;
    private String image;
    private String camp;

    public People(){};

    public People(String name, String gender, String subname,String bornPlace,String bornDate,String deadDate,String info)
    {
        this.name = name;
        this.gender = gender;
        this.subname = subname;
        this.bornPlace = bornPlace;
        this.bornDate = bornDate;
        this.deadDate = deadDate;
        this.info = info;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public void setBornPlace(String bornPlace) {
        this.bornPlace = bornPlace;
    }

    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    public void setDeadDate(String deadDate) {
        this.deadDate = deadDate;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCamp(String camp) { this.camp = camp; }

    public void setPeople(People person)
    {
        this.name = person.getName();
        this.gender = person.getGender();
        this.subname = person.getSubname();
        this.bornPlace = person.getBornPlace();
        this.bornDate = person.getBornDate();
        this.deadDate = person.getDeadDate();
        this.info = person.getInfo();
        this.image = person.getImage();
        this.camp = person.getCamp();
    }

    public int get_id() {
        return _id;
    }

    public String getBornDate() {
        return bornDate;
    }

    public String getBornPlace() {
        return bornPlace;
    }

    public String getDeadDate() {
        return deadDate;
    }

    public String getGender() {
        return gender;
    }

    public String getImage() {
        return image;
    }

    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    public String getSubname() {
        return subname;
    }

    public String getCamp() { return camp; }
}
