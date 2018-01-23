package com.example.administrator.threekingdomsdictionary;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by a on 2017/10/19.
 */
public class MyViewHolder extends RecyclerView.ViewHolder{
    private SparseArray<View> mViews;//存储子view
    private View mConvertView;//存储list_view
    public MyViewHolder(Context context, View itemView, ViewGroup parent)
    {
        super(itemView);
        mConvertView=itemView;
        mViews=new SparseArray<View>();
    }
    public static MyViewHolder get(Context context,ViewGroup parent, int layoutID)
    {
        View itemView= LayoutInflater.from(context).inflate(layoutID,parent,false);
        MyViewHolder holder=new MyViewHolder(context,itemView,parent);
        return holder;
    }
    public <T extends  View>T getView(int viewId)
    {
        View view=mViews.get(viewId);
        if(view==null)
        {
            view=mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }
}
