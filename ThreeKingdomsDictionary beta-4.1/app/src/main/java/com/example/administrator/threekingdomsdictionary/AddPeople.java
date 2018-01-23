package com.example.administrator.threekingdomsdictionary;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Date;


public class AddPeople extends AppCompatActivity {

    private DBManager mgr;
    private User user;

    private String picture_name;

    protected Button add_layout_back;
    protected EditText add_layout_name;
    protected RadioGroup add_layout_sex_group;
    protected EditText add_layout_second_name;
    protected EditText add_layout_place;
    protected RadioGroup add_layout_belone_group;
    protected EditText add_layout_birth_begin;
    protected EditText add_layout_birth_end;
    protected EditText add_layout_story;
    protected Button add_layout_submit;
    protected Button add_layout_choose_picture;
    protected TextView add_layout_picture_message;
    /**
     * 初始化,获取各个控件实例化对象
     */
    protected void init()
    {
        add_layout_back = (Button) findViewById(R.id.add_layout_back);
        add_layout_name = (EditText) findViewById(R.id.add_layout_name);
        add_layout_sex_group = (RadioGroup) findViewById(R.id.add_layout_sex_group);
        add_layout_second_name = (EditText) findViewById(R.id.add_layout_second_name);
        add_layout_place = (EditText) findViewById(R.id.add_layout_place);
        add_layout_belone_group = (RadioGroup) findViewById(R.id.add_layout_belone_group);
        add_layout_birth_begin = (EditText) findViewById(R.id.add_layout_birth_begin);
        add_layout_birth_end = (EditText) findViewById(R.id.add_layout_birth_end);
        add_layout_story = (EditText) findViewById(R.id.add_layout_story);
        add_layout_submit = (Button) findViewById(R.id.add_layout_submit);
        add_layout_choose_picture=(Button)findViewById(R.id.add_layout_choose_picture);
        add_layout_picture_message=(TextView)findViewById(R.id.add_layout_picture_message);


        user = (User)getIntent().getExtras().getSerializable("user");
        mgr = new DBManager(this,user,false);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mgr.closeDB();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_layout);

        //获取实例化对象
        this.init();

        //设置完成按钮
        add_layout_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                People people_added = new People();
                people_added.setName(add_layout_name.getText().toString());
                if(add_layout_sex_group.getCheckedRadioButtonId() == R.id.add_layout_male)people_added.setGender("男");
                else people_added.setGender("女");
                people_added.setSubname(add_layout_second_name.getText().toString());
                people_added.setBornPlace(add_layout_place.getText().toString());
                switch (add_layout_belone_group.getCheckedRadioButtonId())
                {
                    case R.id.add_layout_belone_shu:
                        people_added.setCamp("蜀");
                        break;

                    case R.id.add_layout_belone_wei:
                        people_added.setCamp("魏");
                        break;

                    case R.id.add_layout_belone_wu:
                        people_added.setCamp("吴");
                        break;
                    case R.id.add_layout_belone_du:
                        people_added.setCamp("独");
                        break;
                    default:
                        people_added.setCamp("蜀");
                        break;
                }
                people_added.setBornDate(add_layout_birth_begin.getText().toString());
                people_added.setDeadDate(add_layout_birth_end.getText().toString());
                people_added.setInfo(add_layout_story.getText().toString());

                final String filePath = add_layout_picture_message.getText().toString();
                if(!filePath.isEmpty())
                {
                    int last_point = filePath.lastIndexOf('.');
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                    picture_name = sdf.format(date);
                    picture_name = picture_name + filePath.substring(last_point);
                    people_added.setImage(picture_name);
//                    Toast.makeText(getApplicationContext(),picture_name,Toast.LENGTH_SHORT).show();
                    new Thread(){
                        @Override
                        public void run()
                        {
                            File source = new File(filePath);
                            File dest = new File(getFilesDir().getPath()+"/"+picture_name);
                            FileChannel inputChannel = null;
                            FileChannel outputChannel = null;
                            try {
                                inputChannel = new FileInputStream(source).getChannel();
                                outputChannel = new FileOutputStream(dest).getChannel();
                                outputChannel.transferFrom(inputChannel,0,inputChannel.size());
                                inputChannel.close();
                                outputChannel.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                else people_added.setImage("");

                mgr.update(people_added,people_added.getName(),people_added.getCamp());


                Intent intent_1 = new Intent(AddPeople.this, PeopleList.class);
                startActivity(intent_1);
                finish();
            }

        });

        add_layout_choose_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(AddPeople.this,PickPicture.class);
                startActivityForResult(intent,100);

            }
        });
        //设置返回按钮
        add_layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_2 = new Intent(AddPeople.this, PeopleList.class);
                startActivity(intent_2);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent)
    {
        if(requestCode==100&&resultCode==101)
        {
            Bundle bundle=intent.getExtras();
            if(bundle!=null)
            {
                String filePath=bundle.get("filePath").toString();
                if(filePath!=null)
                {
                    add_layout_picture_message.setText(filePath);
                }
            }
        }
    }
    @Override
    public void onBackPressed() {  // 返回键被按下
        Intent intent_2 = new Intent(AddPeople.this, PeopleList.class);
        startActivity(intent_2);
        finish();
    }

}
