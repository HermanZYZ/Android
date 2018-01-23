package com.example.administrator.threekingdomsdictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ZYZ on 2017/11/15.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "People.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE IF NOT EXISTS WEI" +
//                "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR, gender VARCHAR, subname VARCHAR, bornPlace VARCHAR," +
//                "bornDate VARCHAR, deadDate VARCHAR, info TEXT, image VARCHAR, camp VARCHAR)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS SHU" +
//                "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR, gender VARCHAR, subname VARCHAR, bornPlace VARCHAR," +
//                "bornDate VARCHAR, deadDate VARCHAR, info TEXT, image VARCHAR, camp VARCHAR)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS WU" +
//                "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR, gender VARCHAR, subname VARCHAR, bornPlace VARCHAR," +
//                "bornDate VARCHAR, deadDate VARCHAR, info TEXT, image VARCHAR, camp VARCHAR)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS DU" +
//                "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR, gender VARCHAR, subname VARCHAR, bornPlace VARCHAR," +
//                "bornDate VARCHAR, deadDate VARCHAR, info TEXT, image VARCHAR, camp VARCHAR)");
        try { //AUTOINCREMENT是自增关键字，仅用于整型
            db.execSQL("CREATE TABLE IF NOT EXISTS User_table" +
                    "(name VARCHAR PRIMARY KEY,password VARCHAR)");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE User_table ADD COLUMN other TEXT");
    }
}
