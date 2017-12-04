package com.example.zyz.lab3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    private static final String STATICACTION = "com.example.zyz.lab3.MyStaticFilter";
    private static final String DYNAMICACTION = "com.example.zyz.lab3.MyDynamicFilter";
    private Items items;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_image,pendingIntent);
        ComponentName componentName = new ComponentName(context,NewAppWidget.class);
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(componentName, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context,Intent intent)
    {
        super.onReceive(context,intent);
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//        Bundle bundle = intent.getExtras();
        if(intent.getAction().equals(STATICACTION))
        {
            items = (Items) intent.getExtras().get("item");
            assert items != null;
            Intent local_intent = new Intent(context,ItemInfo.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("item",items);
            local_intent.putExtras(bundle);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.new_app_widget);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,local_intent,PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setTextViewText(R.id.appwidget_text,items.getName()+"仅售￥ "+items.getPrice());
            remoteViews.setImageViewResource(R.id.widget_image,items.getImg());
            remoteViews.setOnClickPendingIntent(R.id.widget_image,pendingIntent);
            ComponentName componentName = new ComponentName(context,NewAppWidget.class);
            AppWidgetManager.getInstance(context).updateAppWidget(componentName,remoteViews);
        }
    }

}

