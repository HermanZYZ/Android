package com.example.zyz.lab3;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by ZYZ on 2017/11/1.
 */

public class ItemTouchCallBack extends ItemTouchHelper.Callback {

    private final CommonAdapter myAdapter;

    public ItemTouchCallBack(CommonAdapter myAdapter) {
        this.myAdapter =myAdapter;

    }
    /**
     * 获取移动的标记  移动和删除
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //设置移动的标记
        int dragFlag= ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
        //设置删除的标记
        int swipFlag=ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
        //制作两种标记
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlag, swipFlag);
    }

    /**
     *
     * @param recyclerView
     * @param viewHolder   原先的viewHolder
     * @param target       拖动后的viewHolder
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //切换位置
        int oldPosition = viewHolder.getAdapterPosition();
        int newPosition = target.getAdapterPosition();
        //交换位置--
        myAdapter.onMove(oldPosition,newPosition);
        return true;
    }
    /**
     *    左右移动
     * @param viewHolder
     * @param direction
     */

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int adapterPosition = viewHolder.getAdapterPosition();
        myAdapter.swipe(adapterPosition);
    }
}

