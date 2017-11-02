package com.example.zyz.lab3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static com.example.zyz.lab3.R.id.amount;


/**
 * Created by ZYZ on 2017/10/23.
 */

public class MainActivity extends AppCompatActivity {

    final boolean HOME = true;
    private static final String STATICACTION = "com.example.zyz.lab3.MyStaticFilter";
    private static final String DYNAMICACTION = "com.example.zyz.lab3.MyDynamicFilter";
    private View mButtonBar;
    private ListView shopping_list;
    private RecyclerView itemslist;
    private FloatingActionButton floatingbutton;
    private CommonAdapter mAdapter;
    private GoodsListAdapter mAdapter_shopping;
    private Button Pay;
    private TextView Amount;
    private double Total_amount = 0.00;
    List<Items> productList = new ArrayList<>();
    List<Items> shoppingList = new ArrayList<>();

    private Receiver dynamicReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        RegisterRec();
        EventBus.getDefault().register(this);
        Init();
        SelectView();
    }

    private void Init()
    {
        mButtonBar = (View) findViewById(R.id.bottombar);
        shopping_list = (ListView) findViewById(R.id.shopping_list);
        itemslist = (RecyclerView) findViewById(R.id.recyclerview);
        floatingbutton = (FloatingActionButton) findViewById(R.id.floatingactionbutton);
        Pay = (Button) findViewById(R.id.button);
        Amount = (TextView) findViewById(amount);

        String[] itemName = getResources().getStringArray(R.array.item_name);
        String[] price = getResources().getStringArray(R.array.price);
        String[] type = getResources().getStringArray(R.array.type);
        String[] info = getResources().getStringArray(R.array.info);
        String[] img = getResources().getStringArray(R.array.img);

        for(int i=0;i<itemName.length;i++)
        {
            Context ctx=getBaseContext();
            int temp = getResources().getIdentifier(img[i],"mipmap",ctx.getPackageName());
            productList.add(new Items(itemName[i],price[i],type[i],info[i],false,temp,R.color.black));
        }
        Amount.setText(Double.toString(Total_amount));
        setView(HOME);

        Random random = new Random();
        int tmp = random.nextInt(productList.size());
        Intent intentBoardcast = new Intent(STATICACTION);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item",productList.get(tmp));
        intentBoardcast.putExtra("toShoplist",false);
        intentBoardcast.putExtras(bundle);
        sendBroadcast(intentBoardcast);
    }

    private void updateShoppingList()
    {
        shoppingList.clear();
        Total_amount = 0.0;
        for (int i = 0 ;i < productList.size(); i++)
        {
            if (productList.get(i).Is_in_list())
            {
                shoppingList.add(productList.get(i));
                Total_amount += productList.get(i).getNum() * (Double.parseDouble(productList.get(i).getPrice()));
            }
        }
        Amount.setText(Double.toString(Total_amount));
        mAdapter_shopping.notifyDataSetChanged();
    }

    private void SelectView() {

        floatingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemslist.getVisibility() == View.INVISIBLE) /*当前处在购物车界面*/ {
                    setView(HOME);
                } else/*当前处在Home界面*/ {
                    //updateShoppingList();
                    setView(!HOME);
                }
            }
        });
////////////////////////////////////////////////////////////////////////////
        //商品页列表
        itemslist.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommonAdapter<Items>(this, R.layout.item_list, productList) {
            @Override
            public void convert(ViewHolder holder, Items items) {
                TextView name = holder.getView(R.id.item_name);
                name.setText(items.getName());
                TextView first = holder.getView(R.id.circle);
                first.setText(items.getFirstLetter());
            }
        };
        mAdapter.setOnItemClickListener(
                new CommonAdapter.OnItemClickListener() {
                    //跳转到商品详细页面
                    @Override
                    public void onClick(int position) {
                        Intent newInten = new Intent().setClass(MainActivity.this, ItemInfo.class);
                        newInten.putExtra("toShoplist",false);
                        newInten.putExtra("item", productList.get(position));
                        startActivityForResult(newInten, 1);//启动intent
                    }

//                    @Override
//                    public void onLongClick(final int position) { //长按事件
//                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                        builder.setTitle("移除商品")
//                                .setMessage("确认移除"+productList.get(position).getName()+"？")
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int which) {
//                                        productList.remove(position);
//                                        mAdapter.notifyDataSetChanged();
//                                        Toast.makeText(MainActivity.this,"移除第"+ (position + 1) +"个商品",Toast.LENGTH_SHORT).show();
//                                        updateShoppingList();
//                                    }
//                                })
//                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                    }
//                                });
//                        builder.create().show();
//                    }
                }
        );
        ItemTouchCallBack itemTouchCallBack = new ItemTouchCallBack(mAdapter);
        //设置给条目触摸的帮助类
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallBack);
        itemTouchHelper.attachToRecyclerView(itemslist);
        itemslist.setAdapter(mAdapter);
//////////////////////////////////////////////////////////////////////////////////////////////////
        //购物车列表
        //shopping_list  ListView
        //shoppList List
        //mAdapter_shopping CommonAdapter
        mAdapter_shopping = new GoodsListAdapter(this,shoppingList);
        mAdapter_shopping.setOnItemClickLitener(new GoodsListAdapter.OnItemClickLitener(){
            @Override
            public void onItemClick(int position){
            // public void onItemClick(AdapterView<?> adapter,View view,int position,long l) {
                Intent newInten = new Intent().setClass(MainActivity.this, ItemInfo.class);
                newInten.putExtra("toShoplist",false);
                newInten.putExtra("item", productList.get(position));
                startActivityForResult(newInten, 2);//启动intent
            }
            @Override
            public void onItemLongClick(final int position)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("移除商品")
                        .setMessage("从购物车移除"+shoppingList.get(position).getName()+"？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                productList.get(productList.indexOf(shoppingList.get(position))).setIs_in_list(false);
                                productList.get(productList.indexOf(shoppingList.get(position))).setNum(0);
                                updateShoppingList();
                                Toast.makeText(MainActivity.this,"移除第" + (position + 1) +"个商品",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        });
                builder.create().show();
            }
        });
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headView = inflater.inflate(R.layout.shopping_list_header, null);
        shopping_list.addHeaderView(headView);
        shopping_list.setAdapter(mAdapter_shopping);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        Items items = (Items)data.getExtras().get("item");
//        productList.get(productList.indexOf(items)).setIs_favor(items.getIs_favor());
//        updateShoppingList();
//    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        boolean check = intent.getBooleanExtra("toShoplist",false);
        if(check)
            setView(!HOME);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        UnregisterRec();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent messageEvent)
    {
        Items items = messageEvent.getItems();
        assert items != null;
        int position = 0;
        for(;position < productList.size();position++)
        {
            if(productList.get(position).getName().equals(items.getName()))
                break;
        }
        productList.get(position).setIs_in_list(items.getIs_in_list());
        productList.get(position).setIs_favor(items.getIs_favor());
        productList.get(position).setNum(items.getNum());
        updateShoppingList();
    }

    void RegisterRec()
    {
        dynamicReceive = new Receiver();
        IntentFilter dynamicFilter = new IntentFilter();
        dynamicFilter.addAction(DYNAMICACTION);
        registerReceiver(dynamicReceive,dynamicFilter);
    }

    void UnregisterRec()
    {
        if(dynamicReceive != null)
        {
            unregisterReceiver(dynamicReceive);
            dynamicReceive = null;
        }
    }

    private void setView(boolean view)
    {
        if(view == HOME)
        {
            floatingbutton.setImageResource(R.drawable.shoplist);
            itemslist.setVisibility(View.VISIBLE);
            shopping_list.setVisibility(View.INVISIBLE);
            mButtonBar.setVisibility(View.INVISIBLE);
        }
        else
        {
            floatingbutton.setImageResource(R.drawable.mainpage);
            itemslist.setVisibility(View.INVISIBLE);
            shopping_list.setVisibility(View.VISIBLE);
            mButtonBar.setVisibility(View.VISIBLE);
        }
    }
}
