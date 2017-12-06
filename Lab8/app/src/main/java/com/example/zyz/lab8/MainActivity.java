package com.example.zyz.lab8;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.media.CamcorderProfile.get;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private Button add;
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, String>> datas;

    private TextView setname;
    private EditText setbirth;
    private EditText setgift;
    private TextView setnumber;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(getBaseContext());
        ElemBinding();
        EvenBinding();
        Init();
    }

    private void Init()
    {
        List<Person> persons = dbHelper.query();
        Map<String, String> map;
        datas = new ArrayList<Map<String,String>>();
        for (Person person : persons)
        {
            map = new HashMap<String, String>();
            map.put("name", person.getName());
            map.put("birth", person.getBirth());
            map.put("gift", person.getGift());
            datas.add(map);
        }
        simpleAdapter = new SimpleAdapter(MainActivity.this, datas,
                R.layout.item,new String[]{"name", "birth", "gift"},
                new int[]{R.id.Name, R.id.Birth, R.id.Gift});
        listView.setAdapter(simpleAdapter);
    }

    private void ElemBinding()
    {
        add = (Button) findViewById(R.id.add);
        listView = (ListView) findViewById(R.id.list);

        LayoutInflater dialog = LayoutInflater.from(MainActivity.this);
        builder = new AlertDialog.Builder(MainActivity.this);
        View newView = dialog.inflate(R.layout.dialog, null);
        setname = (TextView) newView.findViewById(R.id.nameInput);
        setbirth = (EditText) newView.findViewById(R.id.birthInput);
        setgift = (EditText) newView.findViewById(R.id.giftInput);
        setnumber = (TextView) newView.findViewById(R.id.number);
        builder.setView(newView);
        builder.setTitle("修改联系人信息");
    }

    private void EvenBinding()
    {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addPerson.class);
                startActivityForResult(intent, 0);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ElemBinding();
                try {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
                    }

                    setname.setText(datas.get(position).get("name"));
                    setbirth.setText(datas.get(position).get("birth"));
                    setgift.setText(datas.get(position).get("gift"));
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    String tmp = "";
                    Cursor c1 = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                    while (c1.moveToNext()) {
                        String tmp2 = c1.getString(c1.getColumnIndex("_id"));
                        if (c1.getString(c1.getColumnIndex("display_name")).equals(setname.getText().toString())) {
                            if (Integer.parseInt(c1.getString(c1.getColumnIndex("has_phone_number"))) > 0) {
                                Cursor c2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id = " + tmp2, null, null);
                                while (c2.moveToNext()) {
                                    tmp = tmp + c2.getString(c2.getColumnIndex("data1")) + "\n";
                                }
                                c2.close();
                            }
                        }
                    }
                    c1.close();
                    if (tmp.isEmpty()) tmp = "None";
                    setnumber.setText(tmp);
                }catch (Exception e){
                    e.printStackTrace();
                }

                builder.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String birth = setbirth.getText().toString();
                        String gift = setgift.getText().toString();
                        String name = setname.getText().toString();
                        Person person = new Person(name, birth, gift);
                        dbHelper.update(person);
                        Init();
                        Toast.makeText(MainActivity.this,"Finished",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                try {

                    builder.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder message = new AlertDialog.Builder(MainActivity.this);
                message.setTitle("删除信息");
                message.setMessage("请确认是否删除该信息");
                message.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = datas.get(position).get("name");
                        dbHelper.delete(name);
                        datas.remove(position);
                        simpleAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                    }
                });
                message.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                message.create().show();
                return true;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Init();
    }
}
