package com.example.administrator.threekingdomsdictionary;

import java.io.Serializable;

public class People implements Serializable {

    private int _id;
    private String name;
    private String gender;
    private String bornDate;	//生卒年
    private String deadDate;
    private String bornPlace;
    private String info;
    private String subname;
    private String camp;
    private String image;

    public People(){}
    public People(String name, String subname, String gender, String bornDate, String deadDate, String bornPlace, String info,String camp)
    {
        setName(name);
        setSubname(subname);
        setGender(gender);
        setBornDate(bornDate);
        setDeadDate(deadDate);
        setBornPlace(bornPlace);
        setInfo(info);
        setCamp(camp);
    }

    public void setCamp(String camp)
    {
        this.camp = camp;
    }

    public void setId(int _id)
    {
        this._id = _id;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    public void setDeadDate(String deadDate) {
        this.deadDate = deadDate;
    }

    public void setBornPlace(String bornPlace) {
        this.bornPlace = bornPlace;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setSubname(String subname)
    {
        this.subname = subname;
    }

    final public String getName() {
        return name;
    }

    final public String getGender() {
        return gender;
    }

    final public String getBornDate() {
        return bornDate;
    }

    final public String getDeadDate() {
        return deadDate;
    }

    final public String getBornPlace() {
        return bornPlace;
    }

    final public String getInfo() {
        return info;
    }

    final public String getSubname()
    {
        return subname;
    }

    final public int getId()
    {
        return _id;
    }

    final public String getImage()
    {
        return image;
    }

    final public String getCamp()
    {
        return camp;
    }

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


}
