package com.example.administrator.threekingdomsdictionary;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.InputStream;
import java.util.List;

/**
 * Created by ZYZ on 2017/11/20.
 */

public class XML{
    private static final String TAG = "XML";
    private PeopleParser parser;
    private List<People> peoples;

    public XML(Context context)
    {
        try {
            InputStream is = context.getAssets().open("peoples.xml");
            parser = new SaxPeopleParser();  //创建SaxPeopleParser实例
            peoples = parser.parse(is);  //解析输入流
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public List<People> getPeoples() {
        return peoples;
    }
}
