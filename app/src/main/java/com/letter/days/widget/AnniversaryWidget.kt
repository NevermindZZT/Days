package com.letter.days.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import androidx.preference.PreferenceManager
import com.letter.days.R
import com.letter.days.activity.AnniEditActivity
import com.letter.days.activity.AnniversaryActivity
import com.letter.days.database.AppDatabase
import com.letter.days.utils.getClosestAnniversary
import com.letter.days.utils.getDateString
import com.letter.days.utils.getDayText
import com.letter.days.utils.getTypeText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Implementation of App Widget functionality.
 */
class AnniversaryWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        GlobalScope.launch {
            appWidgetIds?.forEach {
                AppDatabase.instance(context?.applicationContext!!)
                    .widgetDao()
                    .deleteById(it)
            }
        }
    }
}

fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    GlobalScope.launch {
        val widget = AppDatabase.instance(context.applicationContext)
            .widgetDao()
            .get(appWidgetId)
            ?: return@launch
        val anniversary =
            (if (widget.anniversaryId == -1) {
                getClosestAnniversary(context)
            } else {
                AppDatabase.instance(context.applicationContext)
                    .anniversaryDao()
                    .get(widget.anniversaryId)
            })
        withContext(Dispatchers.Main) {
            val views = RemoteViews(context.packageName, R.layout.widget_anniversary)

            views.setTextViewText(R.id.name_text, anniversary?.name)
            views.setTextViewText(R.id.date_text, anniversary?.getDateString())
            views.setTextViewText(R.id.type_text, anniversary?.getTypeText())
            views.setTextViewText(R.id.day_text, anniversary?.getDayText())

            val itemIntent = Intent(context, AnniversaryActivity::class.java)
            itemIntent.putExtra("anniId", anniversary?.id)
            val itemPI = PendingIntent.getActivity(context,
                (0..Int.MAX_VALUE).random(),
                itemIntent,
                PendingIntent.FLAG_CANCEL_CURRENT)
            views.setOnClickPendingIntent(R.id.anni_layout, itemPI)

            val addIntent = Intent(context, AnniEditActivity::class.java)
            addIntent.putExtra("anniId", -1)
            val addPI = PendingIntent.getActivity(context,
                (0..Int.MAX_VALUE).random(),
                addIntent,
                PendingIntent.FLAG_CANCEL_CURRENT)
            views.setOnClickPendingIntent(R.id.add_image, addPI)

            if (widget.anniversaryId == -1) {
                views.setViewVisibility(R.id.title_layout, View.GONE)
            }

            views.setViewVisibility(
                R.id.back_layout,
                if (PreferenceManager
                        .getDefaultSharedPreferences(context)
                        .getBoolean("widget_background", false))
                    View.GONE
                else
                    View.VISIBLE)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}