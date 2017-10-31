package com.example.zyz.lab3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RemoteViews;

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
                    .setSmallIcon(R.mipmap.logo)                                           //设置通知小ICON
                    .setColor(Color.parseColor("#FF0000"))             //设置小图标颜色
                    .setAutoCancel(true);                                        //用户单击面板即可取消通知
            Intent mintent = new Intent(context,ItemInfo.class);
            mintent.putExtras(intent.getExtras());
            Bundle bundle = new Bundle();
            bundle.putSerializable("item",item);
            mintent.putExtras(bundle);
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
            builder.setDefaults(Notification.DEFAULT_ALL)
                    .setContentTitle("马上下单")                                //设置通知栏标题
                    .setContentText(item.getName()+"已添加到购物车")   //设置通知栏内容
                    .setTicker("您有一条新消息")                                 //通知首次出现在通知栏，带上升动画效果
                    .setLargeIcon(bitmap)                                        //设置通知大ICON
                    .setSmallIcon(R.mipmap.logo)                                           //设置通知小ICON
                    .setColor(Color.parseColor("#FF0000"))             //设置小图标颜色
                    .setAutoCancel(true);                                        //用户单击面板即可取消通知
            Intent mintent = new Intent(context,MainActivity.class);
            mintent.putExtra("toShoplist",true);
            Bundle bundle = new Bundle();
            bundle.putSerializable("items",item);
            mintent.putExtras(bundle);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context,0,mintent,0);
            builder.setContentIntent(mPendingIntent);
            //绑定notification，发送通知请求
            Notification notification = builder.build();
            manager.notify(dynamication++, notification);

            Intent local_intent = new Intent(context,MainActivity.class);
            local_intent.putExtra("toShoplist",true);
            Bundle bundle_wedget = new Bundle();
            bundle.putSerializable("items",item);
            local_intent.putExtras(bundle_wedget);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.new_app_widget);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,local_intent,PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setTextViewText(R.id.appwidget_text,item.getName()+"已加到购物车，马上下单");
            remoteViews.setImageViewResource(R.id.widget_image,item.getImg());
            remoteViews.setOnClickPendingIntent(R.id.widget_image,pendingIntent);
            ComponentName componentName = new ComponentName(context,NewAppWidget.class);
            AppWidgetManager.getInstance(context).updateAppWidget(componentName,remoteViews);
        }
    }

}
