package com.example.zyz.lab3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZYZ on 2017/10/23.
 */
public class ShoppingList  extends AppCompatActivity {

    private ListView shoppingList;
    private List<Items> data = new ArrayList<>();
    private List<Map<String, Object>> listItems = new ArrayList<>();
    private SimpleAdapter simpleAdapter;
    private AlertDialog.Builder builder;

    private GoodsListAdapter adapter;  //  拓展Adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initialData();
        setListener();
    }

    private void findView() {
        setContentView(R.layout.item_list);
        shoppingList = (ListView) findViewById(R.id.shopping_list);
        builder = new AlertDialog.Builder(this);
    }

    private void initialData() {

        for (Items c : data) {
            Map<String, Object> listItem = new LinkedHashMap<>();
            listItem.put("firstLetter", c.getFirstLetter());
            listItem.put("name", c.getName());
            listItem.put("price",c.getPrice());
            listItems.add(listItem);
        }
        simpleAdapter = new SimpleAdapter(this, listItems, R.layout.list_style,
                new String[] {"firstLetter", "name","price"}, new int[] {R.id.circle, R.id.item_name,R.id.price});
        shoppingList.setAdapter(simpleAdapter);

        //  拓展版写法
//        adapter = new ContactsListAdapter(this, data);
//        contactsList.setAdapter(adapter);

        builder.setTitle("移除商品");
    }

    private void setListener() {

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ShoppingList.this, ItemInfo.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("contacts", data.get(i));
                //  拓展版的单击这样写会好一点
//                bundle.putSerializable("contacts", adapter.getDataList().get(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        shoppingList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                builder.setMessage("从购物车移除" + data.get(pos).getName() + "？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listItems.remove(pos);
                        data.remove(pos);
                        simpleAdapter.notifyDataSetChanged();
                    }
                }).create().show();
                return true;
            }
        });

        // 拓展版的长按写法
//        contactsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
//                builder.setMessage("从购物车移除" + adapter.getDataList().get(pos).getName() + "？");
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        adapter.getDataList().remove(pos);
//                        adapter.notifyDataSetChanged();
//                    }
//                }).create().show();
//                return true;
//            }
//        });
    }
}

