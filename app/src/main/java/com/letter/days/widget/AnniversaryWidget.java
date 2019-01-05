package com.letter.days.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.letter.days.R;
import com.letter.days.activity.AddItemActivity;
import com.letter.days.activity.AnniversaryActivity;
import com.letter.days.anniversary.AnniUtils;
import com.letter.days.anniversary.Anniversary;

import org.litepal.LitePal;

import java.util.List;
import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class AnniversaryWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        freshWidget(context, appWidgetManager, appWidgetId);

//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.anniversary_widget);
//        Anniversary anniversary = AnniUtil.getClosestAnni();
//        views.setTextViewText(R.id.anni_text, anniversary.getText());
//        views.setTextViewText(R.id.anni_date, anniversary.getDateText());
//        views.setTextViewText(R.id.anni_type, anniversary.getTypeText());
//        views.setTextViewText(R.id.anni_days, anniversary.getDaysText());
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
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
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        List<WidgetInfo> widgetInfoList = LitePal.findAll(WidgetInfo.class);
        if (widgetInfoList.size() == 0) {
            return;
        }
        for (WidgetInfo widgetInfo : widgetInfoList) {
            freshWidget(context, manager, widgetInfo.getWidgetId());
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            List<WidgetInfo> widgetInfoList = LitePal.where("widgetId = ?", String.valueOf(appWidgetId)).find(WidgetInfo.class);
            for (WidgetInfo widgetInfo : widgetInfoList) {
                LitePal.delete(WidgetInfo.class, widgetInfo.getId());
            }
        }
    }

    private static void freshWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        List<WidgetInfo> widgetInfoList = LitePal.where("widgetId = ?", String.valueOf(widgetId)).find(WidgetInfo.class);
        if (widgetInfoList.size() != 0) {
            for (WidgetInfo widgetInfo : widgetInfoList) {
                if (widgetInfo != null) {
                    Anniversary anniversary;
                    RemoteViews views;
                    if (widgetInfo.getType() == WidgetInfo.WIDGET_TYPE_CLOSEST) {
                        anniversary = AnniUtils.getClosestAnni();
                        views = new RemoteViews(context.getPackageName(), R.layout.anniversary_widget_closest);
                        Intent intentAdd = new Intent(context, AddItemActivity.class);
                        PendingIntent pendingIntentAdd = PendingIntent.getActivity(context, new Random().nextInt(), intentAdd, 0);
                        views.setOnClickPendingIntent(R.id.widget_add, pendingIntentAdd);
                    } else {
                        anniversary = LitePal.find(Anniversary.class, widgetInfo.getAnniId());
                        views = new RemoteViews(context.getPackageName(), R.layout.anniversary_widget);
                    }
                    views.setTextViewText(R.id.anni_text, anniversary.getText());
                    views.setTextViewText(R.id.anni_date, anniversary.getDateText());
                    views.setTextViewText(R.id.anni_type, anniversary.getTypeText());
                    views.setTextViewText(R.id.anni_days, anniversary.getDaysText());
                    Intent intentItem = new Intent(context, AnniversaryActivity.class);
                    intentItem.putExtra("anniId", anniversary.getId());
                    PendingIntent pendingIntentItem = PendingIntent.getActivity(context, new Random().nextInt(), intentItem, PendingIntent.FLAG_CANCEL_CURRENT);
                    views.setOnClickPendingIntent(R.id.anni_widget_item, pendingIntentItem);
                    appWidgetManager.updateAppWidget(widgetId, views);
                }
            }
        }

    }

}

