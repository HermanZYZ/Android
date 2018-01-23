package com.example.administrator.threekingdomsdictionary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eminayar.panter.DialogType;
import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.interfaces.OnSingleCallbackConfirmListener;
import com.eminayar.panter.interfaces.OnTextInputConfirmListener;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigInput;
import com.mylhyl.circledialog.params.InputParams;
import com.mylhyl.circledialog.view.listener.OnInputClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.ArrayList;
import java.util.Iterator;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PeopleDetail extends AppCompatActivity {

    private ArrayList<TextView> textViews_list;
    private DBManager mgr;
    private User user;//登录的用户

    //peoplr_detail各个控件
    protected Button people_detail_back;
    protected ImageView people_detail_picture;
    protected TextView people_detail_name;
    protected TextView people_detail_second_name;
    protected TextView people_detail_gender;
    protected TextView people_detail_born_date;
    protected TextView people_detail_dead_date;
    protected TextView people_detail_born_place;
    protected TextView people_detail_info;
    protected TextView people_detail_camp;
    protected FloatingActionButton people_detail_edit_or_add;
    protected People selected_people;


    //初始化 获取实例
    protected void init()
    {


        people_detail_back = (Button) findViewById(R.id.people_detail_back);
        people_detail_picture = (ImageView) findViewById(R.id.people_detail_picture);
        people_detail_name = (TextView) findViewById(R.id.people_detail_name);
        people_detail_gender = (TextView) findViewById(R.id.people_detail_sex);
        people_detail_born_date = (TextView) findViewById(R.id.people_detail_birth1);
        people_detail_dead_date = (TextView) findViewById(R.id.people_detail_birth2);
        people_detail_born_place = (TextView) findViewById(R.id.people_detail_place);
        people_detail_info = (TextView) findViewById(R.id.people_detail_story);
        people_detail_edit_or_add = (FloatingActionButton) findViewById(R.id.people_detail_edit_or_add);
        people_detail_camp = (TextView) findViewById(R.id.people_detail_camp);
        people_detail_second_name=(TextView)findViewById(R.id.people_detail_second_name) ;

        textViews_list = new ArrayList<>();
        textViews_list.add(people_detail_name);
//        textViews_list.add(people_detail_gender);
        textViews_list.add(people_detail_born_date);
        textViews_list.add(people_detail_dead_date);
        textViews_list.add(people_detail_born_place);
        textViews_list.add(people_detail_info);
        textViews_list.add(people_detail_second_name);

    }

    protected void getPeopleData(String name,String camp)
    {
        selected_people = new People();
        selected_people.setName(name);
        selected_people.setCamp(camp);
        selected_people = mgr.query(selected_people);
    }

    //设置可编辑内容的textview
    protected void setEditCondition()
    {
        int cnt = 0;
        for(final TextView text1:textViews_list)
        {
            final int tmp = cnt;
            text1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    final EditText et = new EditText(getApplicationContext());
//                    et.setTextColor(Color.rgb(0,0,0));
//
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(PeopleDetail.this);
//                    et.setText(text.getText());
//                                        builder.setTitle("请输入新内容")
//                            .setView(et)
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    text.setText(et.getText().toString());
//                                    updatePeople(text.getText().toString(),tmp);
//                                }
//                            })
//                            .setNegativeButton("取消",null)
//                            .create()
//                            .show();
                String currentString="";
                    if(tmp==0) currentString="名字";
                    else if(tmp==1) currentString="出生年份";
                    else if(tmp==2) currentString="逝世年份";
                    else if(tmp==3) currentString="籍贯";
                    else if(tmp==4) currentString="人物事迹";
                    else if(tmp==5) currentString="表字";

                PanterDialog mPanterDialog=new PanterDialog(PeopleDetail.this);
                mPanterDialog.setHeaderBackground(R.color.blue_btn_bg_color).setTitle("请输入"+currentString+"：")
                             .setDialogType(DialogType.INPUT)
                             .input("请在这里输入要修改的新"+currentString, new OnTextInputConfirmListener() {
                                 @Override
                                 public void onTextInputConfirmed(String input) {
                                     text1.setText(input);
                                     updatePeople(text1.getText().toString(),tmp);
                                 }
                             })
                             .setPositive("确定")
                             .setNegative("取消")
                             .show();
//                    CircleDialog.Builder mBuilder=new CircleDialog.Builder(PeopleDetail.this);
//
//                    mBuilder.setTitle("请输入新内容")
//                            .setCancelable(true)
//                            .configInput(new ConfigInput() {
//                                @Override
//                                public void onConfig(InputParams params) {
//
//
//                                }
//                            })
//                            .setPositiveInput("确定", new OnInputClickListener() {
//                                @Override
//                                public void onClick(String text, View v) {
//                                    text1.setText(text);
//                                    updatePeople(text1.getText().toString(),tmp);
//                                }
//                            })
//                            .setNegative("取消",null)
//                            .show();


                    return true;
                }
            });
            cnt++;
        }

        people_detail_gender.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int checkedID = 0;//0 为男性 1为女性
                if(people_detail_gender.getText().equals("女"))checkedID = 1;
                CircleDialog.Builder mBuilder=new CircleDialog.Builder(PeopleDetail.this);
                PanterDialog mPanterDialog=new PanterDialog(PeopleDetail.this);
                mPanterDialog.setHeaderBackground(R.color.blue_btn_bg_color).setTitle("请选择性别")
                        .setTitleColor(Color.rgb(100,100,100))
                        .setDialogType(DialogType.SINGLECHOICE).isCancelable(true)
                        .items(new String[]{"男", "女"}, new OnSingleCallbackConfirmListener() {
                            @Override
                            public void onSingleCallbackConfirmed(PanterDialog dialog, int pos, String text) {
                                people_detail_gender.setText(text);
                                updatePeople(text,10);
                            }
                        })
                        .setPositive("确定")
                        .setNegative("取消")
                        .show();

//                AlertDialog.Builder builder = new AlertDialog.Builder(PeopleDetail.this);
//                builder.setTitle("请选择性别")
//                        .setSingleChoiceItems(new String[]{"男","女"},checkedID,null)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                int checked = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
//                                String gender_select = ((AlertDialog) dialog).getListView().getItemAtPosition(checked).toString();
//                                people_detail_gender.setText(gender_select);
//                                updatePeople(gender_select,10);
//                            }
//                        })
//                        .setNegativeButton("取消",null)
//                        .create()
//                        .show();


                return true;
            }
        });

        people_detail_camp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int checkedID = 0;
                if(people_detail_camp.getText().equals("魏"))checkedID = 1;
                else if(people_detail_camp.getText().equals("吴"))checkedID = 2;
                else if(people_detail_camp.getText().equals("独"))checkedID = 3;
                PanterDialog mPanterDialog=new PanterDialog(PeopleDetail.this);
                mPanterDialog.setHeaderBackground(R.color.blue_btn_bg_color).setTitle("请选择阵营")
                        .setTitleColor(Color.rgb(100,100,100))
                        .setDialogType(DialogType.SINGLECHOICE).isCancelable(true)
                        .items(new String[]{"蜀", "魏", "吴", "独"}, new OnSingleCallbackConfirmListener() {
                            @Override
                            public void onSingleCallbackConfirmed(PanterDialog dialog, int pos, String text) {
                                people_detail_camp.setText(text);
                                updatePeople(text,11);
                            }
                        })
                        .setPositive("确定")
                        .setNegative("取消")
                        .show();

//                AlertDialog.Builder builder = new AlertDialog.Builder(PeopleDetail.this);
//                builder.setTitle("请选择阵营")
//                        .setSingleChoiceItems(new String[]{"蜀","魏","吴","独"},checkedID,null)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                int checked = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
//                                String select = ((AlertDialog) dialog).getListView().getItemAtPosition(checked).toString();
//                                people_detail_camp.setText(select);
//                                updatePeople(select,11);
//                            }
//                        })
//                        .setNegativeButton("取消",null)
//                        .create()
//                        .show();
                return true;
            }
        });

    }

    //设置update项目
    private void updatePeople(String change,int num)
    {
        String oldName = selected_people.getName();
        String oldCamp = selected_people.getCamp();
        switch (num)
        {
            case 0:
                selected_people.setName(change);
                break;

            case 1:
                selected_people.setBornDate(change);
                break;

            case 2:
                selected_people.setDeadDate(change);
                break;

            case 3:
                selected_people.setBornPlace(change);
                break;

            case 4:
                selected_people.setInfo(change);
                break;

            case 5:
                selected_people.setSubname(change);
                break;

            case 10:
                selected_people.setGender(change);
                break;

            case 11:
                selected_people.setCamp(change);
                break;

            default:
                break;
        }
        mgr.update(selected_people,oldName,oldCamp);
    }

    //设置人物数据
    private void setPeopleDetail()
    {

        people_detail_name.setText((CharSequence) selected_people.getName());
        people_detail_second_name.setText((CharSequence)selected_people.getSubname());
        people_detail_gender.setText((CharSequence) selected_people.getGender());
        people_detail_born_date.setText((CharSequence) selected_people.getBornDate());
        people_detail_dead_date.setText((CharSequence) selected_people.getDeadDate());
        people_detail_born_place.setText((CharSequence) selected_people.getBornPlace());
        people_detail_info.setText((CharSequence) selected_people.getInfo());
        people_detail_camp.setText((CharSequence) selected_people.getCamp());
//        people_detail_picture.setImageBitmap(PictureTool.getBitmapFromPath(this, getApplicationContext().getFilesDir()+"/"+selected_people.getImage()));
        String path = getApplicationContext().getFilesDir()+"/"+selected_people.getImage();
        ImageLoader.getInstance().displayImage( ImageDownloader.Scheme.FILE.wrap(path),people_detail_picture);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_detail);

        //获取实例
        init();

        //获取数据
        Bundle bundle_1 = getIntent().getExtras();
        user = (User)bundle_1.getSerializable("user");
        mgr = new DBManager(this,user,false);
        this.getPeopleData(bundle_1.getString("name"),bundle_1.getString("camp"));


        //显示人物数据
        this.setPeopleDetail();

        //设置可编辑内容textview
        setEditCondition();

        //设置编辑按钮
        people_detail_edit_or_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PanterDialog mPanterDialog=new PanterDialog(PeopleDetail.this);
                mPanterDialog.setHeaderBackground(R.color.blue_btn_bg_color).setTitle("长按")
                        .setMessage("长按要编辑的内容进行编辑！")
                        .setPositive("确定")
                        .setNegative("取消")
                        .show();
//                SweetAlertDialog mSweetAlertDialog=new SweetAlertDialog(PeopleDetail.this);
//                mSweetAlertDialog.setContentText("长按要编辑的内容进行编辑！").setTitleText("长按！")
//                        .show();
            }
        });

        //设置返回按钮
        people_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1 = new Intent(PeopleDetail.this, PeopleList.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
                intent_1.putExtras(bundle);
                startActivity(intent_1);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {  // 返回键被按下
        Intent intent_2 = new Intent(PeopleDetail.this, PeopleList.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        intent_2.putExtras(bundle);
        startActivity(intent_2);
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mgr.closeDB();
    }
}
