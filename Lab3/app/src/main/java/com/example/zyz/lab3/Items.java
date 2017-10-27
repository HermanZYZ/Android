package com.example.zyz.lab3;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZYZ on 2017/10/23.
 */

public class Items implements Serializable {

    private String firstLetter;//首字母
    private String name;//商品名
    private String price;//价格
    private String type;//商品属性类型：“作者”/“重量”/“产地”
    private String info;//商品属性信息：作者/重量/产地
    private boolean is_in_list;//是否被添加到购物车
    private boolean is_favor;//是否被收藏
    private int img;//商品图片的id
    private int backgroundColor;//商品背景颜色
    private int Num;
    private List<String> temp;

    public Items(String name, String price, String type, String info, boolean is_in_list,int img, int backgroundColor) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.info = info;
        this.is_in_list = is_in_list;
        this.img = img;
        this.backgroundColor = backgroundColor;
        Num = 0;
        is_favor = false;
        firstLetter = name.charAt(0) + "";
    }

    public boolean Is_in_list()
    {
        return is_in_list;
    }

    public void setIs_in_list(boolean is_in_list)
    {
        this.is_in_list = is_in_list;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public String getInfo() {  return info;  }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getImg(){return img;}

    public void setIs_favor(boolean is_favor){ this.is_favor = is_favor; }

    public boolean getIs_favor(){ return is_favor; }

    public boolean getIs_in_list(){ return is_in_list; }

    public void setNum(int num){ Num = num; }

    public int getNum(){ return Num;}
}

