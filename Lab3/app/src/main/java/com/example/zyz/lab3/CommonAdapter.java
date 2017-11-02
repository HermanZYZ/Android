package com.example.zyz.lab3;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * Created by ZYZ on 2017/10/19.
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> implements ItemTouchHelperAdapter
{
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    private OnItemClickListener mOnItemClickListener =null;

    public T getItems(int position)
    {
        return mDatas == null ? null : mDatas.get(position);
    }

    public void clearData()
    {
        mDatas.clear();
        this.notifyDataSetChanged();
    }

    public void addData(T items) {
        mDatas.add(items);
        this.notifyDataSetChanged();
    }

    public CommonAdapter(Context context, int layoutId, List<T> datas)
    {
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        ViewHolder viewHolder = ViewHolder.get(mContext, parent, mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        convert(holder,mDatas.get(position));

        if(mOnItemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
//                    return false;
//                }
//            });
        }
    }

    public abstract void convert(ViewHolder holder, T t);

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }
    public void removeItem(int position){
        mDatas.remove(position);
        notifyItemRemoved(position);
    }
    public interface OnItemClickListener{
        void onClick(int position);
        //void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }

    public void onMove(int oldPosition, int newPosition) {
        //集合里边数据
        Collections.swap(mDatas, oldPosition, newPosition);
        //刷新适配器
        this.notifyItemMoved(oldPosition, newPosition);
    }
    public void swipe(int position) {
        //移除数据
        mDatas.remove(position);
        //单条目刷新
        this.notifyItemRemoved(position);
    }

}

