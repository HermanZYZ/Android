package com.example.zyz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DBManager mgr;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        //初始化DBManager
        mgr = new DBManager(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
        deleteDatabase("People.db");
    }

    public void add(View view) {

        People person = new People("刘备", "男", "玄德", "广州", "?", "201", "长得没我帅");
        person.setCamp("蜀");
        People person1 = new People("刘禅", "男", "玄德", "广州", "?", "201", "长得没我帅");
        person1.setCamp("蜀");
        People person2 = new People("曹操", "男", "忘了", "广州", "?", "201", "长得没我帅");
        person2.setCamp("魏");
        People person3 = new People("孙权", "男", "忘了", "广州", "?", "201", "长得没我帅");
        person3.setCamp("吴");

//        if(!mgr.add(person))
//            Toast.makeText(MainActivity.this, person.getName() + "已存在", Toast.LENGTH_SHORT).show();
//        //mgr.deleteOldPeople(person);
//        if(!mgr.add(person1))
//            Toast.makeText(MainActivity.this, person.getName() + "已存在", Toast.LENGTH_SHORT).show();
//        if(!mgr.add(person2))
//            Toast.makeText(MainActivity.this, person.getName() + "已存在", Toast.LENGTH_SHORT).show();
//        if(!mgr.add(person3))
//            Toast.makeText(MainActivity.this, person.getName() + "已存在", Toast.LENGTH_SHORT).show();

        person3.setCamp("蜀");

        User user = new User("123","456");
        mgr.add(user);
        User user1 = new User();
        user1 = mgr.query(user);
        Toast.makeText(MainActivity.this,user1.getName()+user1.getPassword(),Toast.LENGTH_SHORT).show();
    }

    public void update(View view) {
        People person = new People();
        person.setName("刘备");
        person.setGender("女");
        person.setCamp("蜀");
        People person3 = new People("孙权", "男", "忘了", "广州", "?", "201", "长得没我帅");
        person3.setCamp("魏");
        mgr.update(person3, person3.getName(),"吴");
    }

    public void delete(View view) {
        People person = new People();
        person.setName("刘禅");
        person.setCamp("蜀");
        mgr.deleteOldPeople(person);
    }

    public void query(View view) {
        List<People> persons = mgr.Query();
        People person1 = new People();
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (People person : persons) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", person.getName());
            map.put("info", person.getGender() + person.getBornPlace() + person.getCamp());
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2,
                new String[]{"name", "info"}, new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }

    public void queryTheCursor(View view) {
        Cursor c = mgr.queryTheCursor();
        startManagingCursor(c);	//托付给activity根据自己的生命周期去管理Cursor的生命周期
        CursorWrapper cursorWrapper = new CursorWrapper(c) {
        };
        //确保查询结果中有"_id"列
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,
                cursorWrapper, new String[]{"name", "info"}, new int[]{android.R.id.text1, android.R.id.text2});
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
