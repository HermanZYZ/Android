package com.example.zyz.lab3;

//private String[] item_name = new String[]{"Enchated Forest","Arla Milk","Devondale Milk","Kindle Oasis",
//        "waitrose 早餐麦片","Mcvitie's 饼干","Ferrero Rocher","Maltesers","Lindt","Borggreve"};
//    private String[] item_price = new String[]{"¥ 5.00","¥ 59.00","¥ 79.00","¥ 2399.00","¥ 179.00","¥ 14.90","¥ 132.59",
//                        "¥ 141.43","¥ 139.43","¥ 28.90"};

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ZYZ on 2017/10/23.
 */

public class GoodsListAdapter extends BaseAdapter {

    private Context context;
    private List<Items> dataList;

    public GoodsListAdapter(Context context, List<Items> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public List<Items> getDataList() {
        return dataList;
    }

    @Override
    public int getCount() {
        if (isNUll()) {
            return 0;
        }
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        if (isNUll()) {
            return null;
        }
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View convertView;//声明一个新的view变量和ViewHolder变量
        ViewHolder viewHolder;
        //当传进来的view为空的时候加载布局，并且创建一个viewHolder，获取布局中的控件
        if (view == null) {
        	//通过inflate方法来加载布局，传进一个需要使用这个adapter的activity
            convertView = LayoutInflater.from(context).inflate(R.layout.list_style, null);
            viewHolder = new ViewHolder();
            viewHolder.circle = (TextView) convertView.findViewById(R.id.circle);
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_name);
            viewHolder.num = (TextView) convertView.findViewById(R.id.item_num);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(viewHolder);
        } else {
        	//如果不是空的view，那么就从已有的viewHolder中取出
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //添加点击事件处理，将点击事件和view进行绑定
        if (mOnItemClickLitener != null)
        {
            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(i);
                }
            });
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickLitener.onItemLongClick(i);
                    return false;
                }
            });
        }
        //从viewHolder中取出相应的对象，然后赋值
        viewHolder.circle.setText(dataList.get(i).getFirstLetter());
        viewHolder.name.setText(dataList.get(i).getName());
        viewHolder.num.setText(Integer.toString(dataList.get(i).getNum()));
        viewHolder.price.setText("￥ "+dataList.get(i).getPrice());
        return convertView;
    }


    private boolean isNUll() {
        return dataList == null;
    }

    interface OnItemClickLitener
    {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private class ViewHolder {
        public TextView circle;
        public TextView name;
        public TextView num;
        public TextView price;
    }
}
