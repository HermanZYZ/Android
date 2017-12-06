package com.example.zyz.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.zyz.lab8.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZYZ on 2017/11/15.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Contacts.db";
    private static final String Table = "Person";
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
        try { //AUTOINCREMENT是自增关键字，仅用于整型
            db.execSQL("CREATE TABLE IF NOT EXISTS Person" +
                    "(name NTEXT PRIMARY KEY,birth NTEXT, gift NTEXT)");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("ALTER TABLE User_table ADD COLUMN other TEXT");
    }

    public void insert(Person person)
    {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("INSERT INTO Person VALUES(?, ?, ?)",
                    new Object[]{String.valueOf(person.getName()), String.valueOf(person.getBirth()), String.valueOf(person.getGift())});
        }catch (Exception e)
        {
            Log.i("TAG", "INSERT");
            e.printStackTrace();
        }
        db.close();
    }

    public void update(Person person)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", person.getName());
        cv.put("birth", person.getBirth());
        cv.put("gift",person.getGift());
        db.update(Table, cv, "name = ?", new String[]{person.getName()});
        db.close();
    }

    public void delete(String person)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete(Table, "name = ?", new String[]{person});
        db.close();
    }

    public Person query(String Name)
    {
        SQLiteDatabase db = getWritableDatabase();
        Person person = new Person(null, null, null);
        Cursor c = db.rawQuery("SELECT * FROM " + Table + " where name = " + Name, null);
        while (c.moveToNext())
        {
            String name = c.getString(c.getColumnIndex("name"));
            String birth = c.getString(c.getColumnIndex("birth"));
            String gift = c.getString(c.getColumnIndex("gift"));
            person = new Person(name, birth, gift);
        }
        c.close();
        db.close();
        return person;
    }

    public List<Person> query()
    {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Person> persons = new ArrayList<Person>();
        Cursor c = db.rawQuery("SELECT * FROM " + Table, null);
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex("name"));
            String birth = c.getString(c.getColumnIndex("birth"));
            String gift = c.getString(c.getColumnIndex("gift"));
            Person person = new Person(name, birth, gift);
            persons.add(person);
        }
        c.close();
        db.close();
        return persons;
    }
}
