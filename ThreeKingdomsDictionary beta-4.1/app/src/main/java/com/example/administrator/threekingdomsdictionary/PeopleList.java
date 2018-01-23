package com.example.administrator.threekingdomsdictionary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.transition.Slide;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.os.Build.VERSION_CODES.N;

public class PeopleList extends AppCompatActivity {
    private static final String TAG = "XML";
    private PeopleParser parser;
    private List<People> peoples;
    private User user;//登录的用户

    //数据库管理员
    private DBManager mgr;

    //people_list_layout_people_list实例及其适配器
    protected RecyclerView people_list_layout_people_list;
    protected CommonAdapter<People> people_list_layout_people_list_adapter;
    protected FloatingActionButton people_list_layout_add_button;

    private List<People> searchPeople;
    private SearchView mSearchView;
    private ListView mListView;
    private ArrayList<String> Name = new ArrayList<String>();
    private ArrayAdapter<String> searchAdapter;
    //数据列表
    protected List<People> people_list;
    protected ResideMenu resideMenu;
    protected ResideMenuItem item;
    protected void init()
    {
        //获取数据库管理员实例化对象
        user = (User) getIntent().getExtras().getSerializable("user");

        mgr = new DBManager(this,user,false);

        //people_list = new ArrayList<>();

        //从数据库获取数据
        getPeopleDate();

        //获取增添按钮实例
        people_list_layout_add_button = (FloatingActionButton) findViewById(R.id.people_list_layout_add_button);
        people_list_layout_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1 = new Intent(PeopleList.this,AddPeople.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
                intent_1.putExtras(bundle);
                startActivityForResult(intent_1,1);
            }
        });

        //获取recylcerView实例
        people_list_layout_people_list = (RecyclerView) findViewById(R.id.people_list_layout_people_list);
        people_list_layout_people_list.setLayoutManager(new LinearLayoutManager(this));
        //设置recylcerView适配器
        people_list_layout_people_list_adapter = new CommonAdapter<People>(this,R.layout.people_list_info,people_list) {
            @Override
            protected void convert(MyViewHolder holder, final People people, final int position) {
                //设置列表人物图片
                final ImageView people_info_picture = holder.getView(R.id.people_info_picture);
                //people_info_picture.setImageBitmap(PictureTool.getBitmapFromPath(PeopleList.this, getApplicationContext().getFilesDir()+"/"+people.getImage()));
                String path = getApplicationContext().getFilesDir()+"/"+people.getImage();
                ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(path),people_info_picture);

                //设置人物姓名
                TextView people_info_name = holder.getView(R.id.people_info_name);
                people_info_name.setText((CharSequence) people.getName());

                //设置人物性别
                TextView people_info_sex = holder.getView(R.id.people_info_sex);
                people_info_sex.setText((CharSequence) people.getGender());

                //设置人物字
                TextView people_info_second_name = holder.getView(R.id.people_info_second_name);
                people_info_second_name.setText((CharSequence) people.getSubname());

                //设置人物生卒年
                TextView people_info_place = holder.getView(R.id.people_info_place);
                people_info_place.setText(people.getBornDate() + " - " + people.getDeadDate());


//                //
//                Button people_info_delete_button=holder.getView(R.id.people_info_delete);
//                people_info_delete_button.setOnClickListener(new View.OnClickListener() {
//                                                                 @Override
//                                                                 public void onClick(View v) {
////                                                                     Toast.makeText(getApplicationContext(),"删除",Toast.LENGTH_LONG).show();
////                                                                     Toast.makeText(getApplicationContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
//                                                                     mgr.deleteOldPeople(people_list.get(position));
//                                                                     people_list.remove(position);
//                                                                     people_list_layout_people_list_adapter.notifyDataSetChanged();
//
//                                                                 }
//                                                             }
//
//
//                );

                //infoView
//                RelativeLayout people_info_relativelayout1 = holder.getView(R.id.people_info_relativelayout1);
//                people_info_relativelayout1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(getApplicationContext(),(CharSequence) people.getName(),Toast.LENGTH_SHORT).show();
//                        Intent intent_1 = new Intent(PeopleList.this,PeopleDetail.class);
//                        Bundle bundle_1 = new Bundle();
//                        bundle_1.putString("name",people.getName());
//                        bundle_1.putString("camp",people.getCamp());
//                        intent_1.putExtras(bundle_1);
//                        startActivityForResult(intent_1,1);
//                    }
//                });
            }
        };

        //设置适配器
        people_list_layout_people_list.setAdapter(people_list_layout_people_list_adapter);

    }

    protected void getPeopleDate()
    {
        people_list = mgr.Query();
    }

    //从其他ACTIVITY回来后 更新数据列表
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        people_list.clear();
        people_list.addAll(mgr.Query());
        people_list_layout_people_list_adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mgr.closeDB();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= N){
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {}
        }

        setContentView(R.layout.people_list_layout);

        Search();

        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        final String titles[] = { "魏国阵营人物", "蜀国阵营人物", "吴国阵营人物","独立阵营人物"};
        int icon[] = { R.drawable.icon_wei, R.drawable.icon_shu, R.drawable.icon_wu,R.drawable.icon_du};

        for (int i = 0; i < titles.length; i++){
            final int tmp = i;
            item = new ResideMenuItem(this, icon[i], titles[i]);
            item.tv_title.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // 显示各阵营人物列表
//                    Toast.makeText(PeopleList.this, "You clicked Button", Toast.LENGTH_SHORT).show();
                    people_list.clear();
                    people_list.addAll(mgr.Query(titles[tmp].substring(0,1)));
                    people_list_layout_people_list_adapter.notifyDataSetChanged();

                }
            });
            item.iv_icon.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // 显示各阵营人物列表
//                    Toast.makeText(PeopleList.this, "You clicked Button", Toast.LENGTH_SHORT).show();
                    people_list.clear();
                    people_list.addAll(mgr.Query(titles[tmp].substring(0,1)));
                    people_list_layout_people_list_adapter.notifyDataSetChanged();

                }
            });
            resideMenu.addMenuItem(item, ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        }

        Slide slideOut = new Slide();
        slideOut.setDuration(500);
        //设置从左边退出
        slideOut.setSlideEdge(android.view.Gravity.LEFT);
        //设置为退出
        slideOut.setMode(Visibility.MODE_OUT);
        getWindow().setExitTransition(slideOut);

        //初始化,获取实例
        this.init();

        //重写适配器选项点击事件
        people_list_layout_people_list_adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent_1 = new Intent(PeopleList.this,PeopleDetail.class);
                Bundle bundle_1 = new Bundle();
                bundle_1.putString("name",people_list.get(position).getName());
                bundle_1.putString("camp",people_list.get(position).getCamp());
                bundle_1.putSerializable("user",user);
                intent_1.putExtras(bundle_1);
                startActivityForResult(intent_1,1);

            }

            @Override
            public void onLongClick(final int position) {
                final SweetAlertDialog mSweetAlertDialog=new SweetAlertDialog(PeopleList.this);
                final String name_=people_list.get(position).getName();
                mSweetAlertDialog.setTitleText("确认人物删除")
                        .setContentText("是否删除"+name_+"?")
                        .setConfirmText("确认删除")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mgr.deleteOldPeople(people_list.get(position));
                                people_list.remove(position);
                                people_list_layout_people_list_adapter.notifyDataSetChanged();

                                sweetAlertDialog.setTitleText("删除成功")
                                        .setContentText("你已经成功删除"+name_)
                                        .setConfirmText("确定")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                            }
                        })
                        .setCancelText("取消")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mSweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
//                people_list.remove(position);
//                people_list_layout_people_list_adapter.notifyDataSetChanged();
            }
        });

        //设置增添按钮点击事件
        people_list_layout_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1 = new Intent(PeopleList.this, AddPeople.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
                intent_1.putExtras(bundle);
                startActivity(intent_1);
            }
        });

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                mListView.setVisibility(View.VISIBLE);
                searchPeople = mgr.SuperQuery(query);
                if(searchPeople.size() == 0)
                    Toast.makeText(PeopleList.this,query + "不存在", Toast.LENGTH_SHORT).show();
                else
                {
                    searchAdapter.clear();
                    for(People people : searchPeople)
                    {
//                        Toast.makeText(PeopleList.this,people.getName(), Toast.LENGTH_SHORT).show();
                        Name.add(people.getName());
                    }
                    searchAdapter.notifyDataSetChanged();
                }
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                mListView.setVisibility(View.VISIBLE);
                people_list_layout_people_list.setVisibility(View.INVISIBLE);
                searchPeople = mgr.SuperQuery(newText);
                if(searchPeople.size() != 0)
                {
                    searchAdapter.clear();
                    for(People people : searchPeople)
                    {
//                        Toast.makeText(PeopleList.this,people.getName(), Toast.LENGTH_SHORT).show();
                        Name.add(people.getName());
                    }
                    searchAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mListView.setVisibility(View.INVISIBLE);
                people_list_layout_people_list.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    private void Search()
    {
        mSearchView = (SearchView) findViewById(R.id.searchView);
        mListView = (ListView) findViewById(R.id.searchListView);
        searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Name);
        mListView.setAdapter(searchAdapter);
        mListView.setTextFilterEnabled(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent_1 = new Intent(PeopleList.this, PeopleDetail.class);
                Bundle bundle_1 = new Bundle();
                bundle_1.putString("name", searchPeople.get(position).getName());
                bundle_1.putString("camp", searchPeople.get(position).getCamp());
                bundle_1.putSerializable("user", user);
                intent_1.putExtras(bundle_1);
                startActivityForResult(intent_1, 1);
            }
        });
    }
}
