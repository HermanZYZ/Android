package com.example.zyz.lab3;

/**
 * Created by ZYZ on 2017/10/27.
 */

public class MessageEvent {
    private Items items;

    public MessageEvent(Items items)
    {
        this.items = items;
    }

//    public abstract void onMessageEvent(Items items);

    public Items getItems()
    {
        return items;
    }
}
