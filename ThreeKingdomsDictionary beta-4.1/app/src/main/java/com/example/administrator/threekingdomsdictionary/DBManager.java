package com.example.administrator.threekingdomsdictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by ZYZ on 2017/11/15.
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;
    private User user;

    public DBManager(Context context, User user, boolean isRegister) {
        helper = new DBHelper(context);
        this.user = user;
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
        if(isRegister)
            addTable(context);
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    /**
     * add and init tables for new users
     * @param context
     * @return
     */
    private boolean addTable(Context context)
    {
        if(this.user.getName() == null || this.user.getPassword() == null)
            return false;
        User userFound = query(this.user);
        if(userFound.getName() != null)
            return false;
        add(this.user);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + user.getName() +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR, gender VARCHAR, subname VARCHAR, bornPlace VARCHAR," +
                "bornDate VARCHAR, deadDate VARCHAR, info TEXT, image VARCHAR, camp VARCHAR)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS SHU" + user.getName() +
//                "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR, gender VARCHAR, subname VARCHAR, bornPlace VARCHAR," +
//                "bornDate VARCHAR, deadDate VARCHAR, info TEXT, image VARCHAR, camp VARCHAR)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS WU" + user.getName() +
//                "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR, gender VARCHAR, subname VARCHAR, bornPlace VARCHAR," +
//                "bornDate VARCHAR, deadDate VARCHAR, info TEXT, image VARCHAR, camp VARCHAR)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS DU" + user.getName() +
//                "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR, gender VARCHAR, subname VARCHAR, bornPlace VARCHAR," +
//                "bornDate VARCHAR, deadDate VARCHAR, info TEXT, image VARCHAR, camp VARCHAR)");

        XML xml = new XML(context);
        InitDB(db, xml);
        return true;

    }

    /**
     * initial the new tables
     * @param db
     * @param xml
     */
    private void InitDB(SQLiteDatabase db, XML xml)
    {
        xml.getPeoples();
        for(People person : xml.getPeoples())
            db.execSQL("INSERT INTO " + user.getName() + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{person.getName(), person.getGender(), person.getSubname(),
                            person.getBornPlace(), person.getBornDate(), person.getDeadDate(),
                            person.getInfo(), person.getImage(), person.getCamp()});
    }

    /**
     * add user
     * @param user
     */
    private boolean add(User user)
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
        if(person.getName() == null)
            return false;
        db.beginTransaction();  //开始事务
        try {
            People personFound = new People();
            personFound = query(person);
            if(personFound.getName() == null) {
                db.execSQL("INSERT INTO "+ user.getName() + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        new Object[]{person.getName(), person.getGender(), person.getSubname(),
                                person.getBornPlace(), person.getBornDate(), person.getDeadDate(),
                                person.getInfo(), person.getImage(), person.getCamp()});
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
        if(person.getName() == null)
            return false;
        People oldPerson = new People();
        oldPerson.setPeople(person);
        oldPerson.setName(oldName);
//        oldPerson.setCamp(oldCamp);
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
        if(person.getName() == null)
            return false;
        db.delete(user.getName(), "name = ?", new String[]{String.valueOf(person.getName())});
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
            person.setId(c.getInt(c.getColumnIndex("_id")));
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
     * query all persons, return list
     * @return List<People>
     */
    public List<People> SuperQuery(String string) {
        ArrayList<People> persons = new ArrayList<People>();
        Cursor c = db.rawQuery("SELECT * FROM " + user.getName()+ " " + " where name like '" + string + "%' " +
                "or name like '%" + string + "%'", null);
        while (c.moveToNext()) {
            People person = new People();
            person.setId(c.getInt(c.getColumnIndex("_id")));
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
     * query all persons from a camp, return list
     * @return List<People>
     */
    public List<People> Query(String camp) {
        ArrayList<People> persons = new ArrayList<People>();
//        Cursor c = db.rawQuery("SELECT * FROM " + user.getName(), null);
        Cursor c = db.query(user.getName(), new String[]{"*"}, "camp=?",
                new String[]{camp}, null, null, null);
        while (c.moveToNext()) {
            People person = new People();
            person.setId(c.getInt(c.getColumnIndex("_id")));
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
        Cursor c = db.query(user.getName(), new String[]{"*"}, "name=?",
                new String[]{person.getName()}, null, null, null);
//        Cursor c = queryTheCursor();
        while (c.moveToNext())
        {
            if(c.getString(c.getColumnIndex("name")).equals(person.getName()))
            {
                personFound.setId(c.getInt(c.getColumnIndex("_id")));
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
        }
        c.close();
        return personFound;
    }

    /**
     * query all persons, return cursor
     * @return  Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM " + user.getName(), null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }

}
