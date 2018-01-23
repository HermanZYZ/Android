package com.example.zyz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by ZYZ on 2017/11/15.
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add user
     * @param user
     */
    public boolean add(User user)
    {
        if(user.getName() == null || user.getPassword() == null)
            return false;
        if(query(user).getName() != null)
            return false;
        else
        {
            try {
                db.execSQL("INSERT INTO User_table VALUES(?, ?)",
                        new Object[]{String.valueOf(user.getName()), String.valueOf(user.getPassword())});
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        return true;
    }

    /**
     * query User
     */
    public User query(User user)
    {
        if(user.getName() == null || user.getPassword() == null)
            return null;
        User userFound = new User();
        try {
            Cursor c = db.query("User_table", new String[]{"*"}, "name=?", new String[]{user.getName()}, null, null, null);

            if(c.moveToNext())
            {
                userFound.setName(c.getString(c.getColumnIndex("name")));
                userFound.setPassword(c.getString(c.getColumnIndex("password")));
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return userFound;
    }

    /**
     * add persons
     * @param person
     */
    public boolean add(People person) {
        if(person.getCamp() == null || person.getName() == null)
            return false;
        db.beginTransaction();  //开始事务
        try {
            People personFound = new People();
            personFound = query(person);
            if(personFound.getName() == null) {
                db.execSQL("INSERT INTO "+ Convert(person.getCamp()) + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, null, ?)",
                        new Object[]{person.getName(), person.getGender(), person.getSubname(),
                                person.getBornPlace(), person.getBornDate(), person.getDeadDate(), person.getInfo(), person.getCamp()});
            }
            else
            {
                db.setTransactionSuccessful();  //设置事务成功完成
                return false;
            }
            db.setTransactionSuccessful();  //设置事务成功完成
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }finally {
            db.endTransaction();    //结束事务
        }
    }
    /**
     * update person
     * @param person
     */
    public boolean update(People person, String oldName, String oldCamp) {
        return updateCamp(person,oldName, oldCamp);
    }

//    /**
//     * update person's name
//     * @param person
//     */
//    public boolean updateName(People person) {
//        if(person.getCamp() == null || person.getName() == null)
//            return false;
//        ContentValues cv = new ContentValues();
//        cv.put("name", person.getName());
//        db.update(Convert(person.getCamp()), cv, "name = ?", new String[]{person.getName()});
//        return true;
//    }
//
//    /**
//     * update person's gender
//     * @param person
//     */
//    public boolean updateGender(People person) {
//        if(person.getCamp() == null || person.getName() == null)
//            return false;
//        ContentValues cv = new ContentValues();
//        cv.put("gender", person.getGender());
//        db.update(Convert(person.getCamp()), cv, "name = ?", new String[]{person.getName()});
//        return true;
//    }
//
//    /**
//     * update person's subname
//     * @param person
//     */
//    public boolean updateSubname(People person) {
//        if(person.getCamp() == null || person.getName() == null)
//            return false;
//        ContentValues cv = new ContentValues();
//        cv.put("subname", person.getSubname());
//        db.update(Convert(person.getCamp()), cv, "name = ?", new String[]{person.getName()});
//        return true;
//    }
//
//    /**
//     * update person's bornplace
//     * @param person
//     */
//    public boolean updateBornPlace(People person) {
//        if(person.getCamp() == null || person.getName() == null)
//            return false;
//        ContentValues cv = new ContentValues();
//        cv.put("bornPlace", person.getBornPlace());
//        db.update(Convert(person.getCamp()), cv, "name = ?", new String[]{person.getName()});
//        return true;
//    }
//
//    /**
//     * update person's deadDate
//     * @param person
//     */
//    public boolean updateDeadDate(People person) {
//        if(person.getCamp() == null || person.getName() == null)
//            return false;
//        ContentValues cv = new ContentValues();
//        cv.put("deadDate", person.getDeadDate());
//        db.update(Convert(person.getCamp()), cv, "name = ?", new String[]{person.getName()});
//        return true;
//    }
//
//    /**
//     * update person's info
//     * @param person
//     */
//    public boolean updateInfo(People person) {
//        if(person.getCamp() == null || person.getName() == null)
//            return false;
//        ContentValues cv = new ContentValues();
//        cv.put("info", person.getInfo());
//        db.update(Convert(person.getCamp()), cv, "name = ?", new String[]{person.getName()});
//        return true;
//    }
//
//    /**
//     * update person's bornDate
//     * @param person
//     */
//    public boolean updateBornDate(People person) {
//        if(person.getCamp() == null || person.getName() == null)
//            return false;
//        ContentValues cv = new ContentValues();
//        cv.put("bornDate", person.getBornDate());
//        db.update(Convert(person.getCamp()), cv, "name = ?", new String[]{person.getName()});
//        return true;
//    }
//
//    /**
//     * update person's image
//     * @param person
//     */
//    public boolean updateImage(People person) {
//        if(person.getCamp() == null || person.getName() == null)
//            return false;
//        ContentValues cv = new ContentValues();
//        cv.put("image", person.getImage());
//        db.update(Convert(person.getCamp()), cv, "name = ?", new String[]{person.getName()});
//        return true;
//    }

    /**
     * update person's camp
     * @param person
     */
    private boolean updateCamp(People person, String oldName, String oldCamp) {
        if(person.getCamp() == null || person.getName() == null)
            return false;
        People oldPerson = new People();
        oldPerson.setPeople(person);
        oldPerson.setName(oldName);
        oldPerson.setCamp(oldCamp);
        if(!deleteOldPeople(oldPerson))//从先前的表中删除
            return false;
        if(!add(person))
            return false;
        return true;
    }

    /**
     * delete old person
     * @param person
     */
    public boolean deleteOldPeople(People person) {
        if(person.getCamp() == null || person.getName() == null)
            return false;
        db.delete(Convert(person.getCamp()), "name = ?", new String[]{String.valueOf(person.getName())});
        return true;
    }

    /**
     * query all persons, return list
     * @return List<People>
     */
    public List<People> Query() {
        ArrayList<People> persons = new ArrayList<People>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            People person = new People();
            person.set_id(c.getInt(c.getColumnIndex("_id")));
            person.setName(c.getString(c.getColumnIndex("name")));
            person.setGender(c.getString(c.getColumnIndex("gender")));
            person.setSubname(c.getString(c.getColumnIndex("subname")));
            person.setBornPlace(c.getString(c.getColumnIndex("bornPlace")));
            person.setBornDate(c.getString(c.getColumnIndex("bornDate")));
            person.setDeadDate(c.getString(c.getColumnIndex("deadDate")));
            person.setInfo(c.getString(c.getColumnIndex("info")));
            person.setImage(c.getString(c.getColumnIndex("image")));
            person.setCamp(c.getString(c.getColumnIndex("camp")));

            persons.add(person);
        }
        c.close();
        return persons;
    }

    /**
     * query one person by name, return People
     * @return People
     */
    public People query(People person)
    {
        People personFound = new People();
        Cursor c = db.query(Convert(person.getCamp()), new String[]{"*"}, "name=?", new String[]{person.getName()}, null, null, null);
        c.getCount();
        if (c.getCount() != 0)
        {
            c.moveToNext();
            personFound.set_id(c.getInt(c.getColumnIndex("_id")));
            personFound.setName(c.getString(c.getColumnIndex("name")));
            personFound.setGender(c.getString(c.getColumnIndex("gender")));
            personFound.setSubname(c.getString(c.getColumnIndex("subname")));
            personFound.setBornPlace(c.getString(c.getColumnIndex("bornPlace")));
            personFound.setBornDate(c.getString(c.getColumnIndex("bornDate")));
            personFound.setDeadDate(c.getString(c.getColumnIndex("deadDate")));
            personFound.setInfo(c.getString(c.getColumnIndex("info")));
            personFound.setImage(c.getString(c.getColumnIndex("image")));
            personFound.setCamp(c.getString(c.getColumnIndex("camp")));
        }
        c.close();
        return personFound;
    }

    /**
     * query all persons, return cursor
     * @return  Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM WEI UNION SELECT * FROM SHU UNION SELECT * FROM WU", null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }

    /**
     * Turn Chinese to pinyin
     */
    private String Convert(String camp)
    {
        if(camp.equals("魏"))
            return "WEI";
        if(camp.equals("蜀"))
            return "SHU";
        if(camp.equals("吴"))
            return "WU";
        return "WEI";
    }
}
