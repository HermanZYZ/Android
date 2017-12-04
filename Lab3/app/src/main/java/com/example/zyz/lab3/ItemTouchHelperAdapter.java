package com.example.zyz.lab3;

/**
 * Created by ZYZ on 2017/11/1.
 */

public interface ItemTouchHelperAdapter {
    void onMove(int oldPosition, int newPosition);
    void swipe(int position);
}
