package com.example.zyz.lab3;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zyz on 17-10-23.
 * 商品详细信息Activity
 */
public class ItemInfo extends AppCompatActivity {

    private RelativeLayout topBack;  //  顶部三分之一
    private ImageView back;  //  返回图标
    private ImageView star;  //  星星图标
    private TextView name;
    private TextView price;
    private TextView type;
    private TextView info;
    private ListView operationList;
    private ImageView addtoshoplist;
    private ImageView img;



    private static final String DYNAMICACTION = "com.example.zyz.lab3.MyDynamicFilter";

    private boolean if_shop_click = false;
    private Resources res;
    private String[] operations;
    private Items items;  //  该页面的商品

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_main_contact);

        //this.position = (int)getIntent().getExtras().get("position");
        findView();
        initialData();
        setListener();
        //ReturnIntent();
    }

    /* 初始化布局  */
    private void findView() {
        topBack = (RelativeLayout) findViewById(R.id.top_back);
        back = (ImageView) findViewById(R.id.back);
        star = (ImageView) findViewById(R.id.star);
        name = (TextView) findViewById(R.id.item_name);
        price = (TextView) findViewById(R.id.show_price);
        type = (TextView) findViewById(R.id.type);
        info = (TextView) findViewById(R.id.info);
        operationList = (ListView) findViewById(R.id.operationlist);
        addtoshoplist = (ImageView) findViewById(R.id.addtoshoplist);
        img = (ImageView) findViewById(R.id.show_image);
    }

    /*	初始化数据     */
    private void initialData() {
        res = getResources();
        items = (Items) getIntent().getExtras().get("item");
        if (items != null) 
        {
            //topBack.setBackgroundColor(getColor(items.getBackgroundColor()));
            name.setText(items.getName());
            price.setText(items.getPrice());
            type.setText(items.getType());
            info.setText(items.getInfo());
            if(items.getIs_favor())
                star.setImageResource(R.drawable.full_star);
            else
                star.setImageResource(R.drawable.empty_star);
            img.setImageResource(items.getImg());
        }
        operations = new String[] {res.getString(R.string.one_key_to_order),res.getString(R.string.share),res.getString(R.string.not_interesting),res.getString(R.string.check_more)};
        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(this, R.layout.operating_list, operations);
        operationList.setAdapter(arrayAdapter);
    }

    /* 设置监听器 */
    private void setListener() {

        /* 返回图标被点击，销毁该Activity */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /* 星星图标被点击，切换  */
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!items.getIs_favor()) {
                    view.setBackground(getDrawable(R.drawable.full_star));
                    items.setIs_favor(true);
                    Toast.makeText(ItemInfo.this,"已添加收藏",Toast.LENGTH_SHORT).show();
                } else {
                    view.setBackground(getDrawable(R.drawable.empty_star));
                    items.setIs_favor(false);
                    Toast.makeText(ItemInfo.this,"已取消收藏",Toast.LENGTH_SHORT).show();
                }
                EventBus.getDefault().post(new MessageEvent(items));
            }
        });

        addtoshoplist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(items);
                Toast.makeText(ItemInfo.this,"商品已添加到购物车",Toast.LENGTH_SHORT).show();
//                Intent newintent = new Intent();
//                newintent.putExtra("addtolist",position);
//                setResult(1,newintent);
                if_shop_click = true;
                items.setIs_in_list(true);
                items.setNum(items.getNum()+1);
                Intent intentBoardcast = new Intent(DYNAMICACTION);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item",items);
                intentBoardcast.putExtra("toShoplist",false);
                intentBoardcast.putExtras(bundle);
                sendBroadcast(intentBoardcast);
                EventBus.getDefault().post(new MessageEvent(items));
            }
        });
    }
}

