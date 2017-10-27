package com.example.zyz.lab3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

/**
 * Created by ZYZ on 2017/10/27.
 */

public class Receiver extends BroadcastReceiver {
    private static final String STATICACTION = "com.example.zyz.lab3.MyStaticFilter";
    private static final String DYNAMICACTION = "com.example.zyz.lab3.MyDynamicFilter";
    static int staticaction = 0;
    static int dynamication = 0;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent.getAction().equals(STATICACTION))
        {
            Items item = (Items) intent.getExtras().get("item");
            assert item != null;
            int img = item.getImg();
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),img);
            //获取通知状态栏管理
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //实例化通知状态栏构造器
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("新商品热卖")                                //设置通知栏标题
                    .setContentText(item.getName()+"仅售"+item.getPrice()+"!")   //设置通知栏内容
                    .setTicker("您有一条新消息")                                 //通知首次出现在通知栏，带上升动画效果
                    .setLargeIcon(bitmap)                                        //设置通知大ICON
                    .setSmallIcon(img)                                           //设置通知小ICON
                    .setAutoCancel(true);                                        //用户单击面板即可取消通知
            Intent mintent = new Intent(context,ItemInfo.class);
            mintent.putExtras(intent.getExtras());
            PendingIntent mPendingIntent = PendingIntent.getActivity(context,0,mintent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mPendingIntent);
            //绑定notification，发送通知请求
            manager.notify(staticaction++, builder.build());
        }
        else if(intent.getAction().equals(DYNAMICACTION))
        {
            Items item = (Items) intent.getExtras().get("item");
            assert item != null;
            int img = item.getImg();
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),img);
            //获取通知状态栏管理
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //实例化通知状态栏构造器
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("马上下单")                                //设置通知栏标题
                    .setContentText(item.getName()+"已添加到购物车")   //设置通知栏内容
                    .setTicker("您有一条新消息")                                 //通知首次出现在通知栏，带上升动画效果
                    .setLargeIcon(bitmap)                                        //设置通知大ICON
                    .setSmallIcon(img)                                           //设置通知小ICON
                    .setAutoCancel(true);                                        //用户单击面板即可取消通知
            Intent mintent = new Intent(context,MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("item",item);
            mintent.putExtras(bundle);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context,0,mintent,0);
            builder.setContentIntent(mPendingIntent);
            //绑定notification，发送通知请求
            Notification notification = builder.build();
            manager.notify(dynamication++, notification);
        }
    }

}
