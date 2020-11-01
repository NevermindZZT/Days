package com.letter.days.utils

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import com.letter.days.R
import com.letter.days.activity.AnniversaryActivity
import com.letter.days.activity.MainActivity
import com.letter.days.database.AppDatabase
import com.letter.days.database.entity.AnniversaryEntity
import com.letter.days.service.CoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val MILLIS_A_MINUTE = 1000 * 60
private const val MILLIS_AN_HOUR = 60 * MILLIS_A_MINUTE

/**
 * 设置纪念日闹钟
 * @param context Context context
 * @param anniversary AnniversaryEntity 纪念日
 */
fun setAnniversaryAlarm(context: Context, anniversary: AnniversaryEntity) {
    val time = anniversary.getNextCalendar().timeInMillis + 9 * MILLIS_AN_HOUR
    if (time > JCalendar.getInstance().timeInMillis) {
        val intent = Intent(context, CoreService::class.java)
        intent.apply {
            putExtra(CoreService.EXTRA_IS_DAYS_NOTIFY, true)
            putExtra(CoreService.EXTRA_ANNI_ID, anniversary.id)
        }
        val pi = PendingIntent.getService(context, anniversary.id, intent, FLAG_UPDATE_CURRENT)
        (context.getSystemService(ALARM_SERVICE) as AlarmManager?)?.set(
            AlarmManager.RTC_WAKEUP,
            time,
            pi
        )
    }
}

/**
 * 设置所有纪念日闹钟
 * @param context Context context
 */
suspend fun setAllAnniversaryAlarm(context: Context) =
    AppDatabase.instance(context)
        .anniversaryDao()
        .getAll()
        .forEach {
            withContext(Dispatchers.Main) {
                setAnniversaryAlarm(context, it)
            }
        }

/**
 * 显示纪念日通知
 * @param context Context context
 * @param anniversary AnniversaryEntity 纪念日
 */
fun showAnniversaryNotification(context: Context, anniversary: AnniversaryEntity) {
    val intent = Intent(context.applicationContext, AnniversaryActivity::class.java)
    intent.apply {
        putExtra("anniId", anniversary.id)
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }
    val pi = PendingIntent.getActivity(context, anniversary.id, intent, FLAG_UPDATE_CURRENT)

    val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context.applicationContext,
                context.getString(R.string.notification_channel_anni_id))
        } else {
            Notification.Builder(context.applicationContext)
        }
    builder.apply {
        setContentTitle(anniversary.getTypeText())
        setContentText(anniversary.name)
        setWhen(System.currentTimeMillis())
        setSmallIcon(R.drawable.ic_notify_small)
        setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round))
        setContentIntent(pi)
        setAutoCancel(true)
    }
    val notification = builder.build()
    (context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?)?.notify(anniversary.id, notification)
}

/**
 * 获取前台通知
 * @param context Context context
 * @return Notification 前台通知
 */
fun getIntentNotification(context: Context): Notification {
    val intent = Intent(context.applicationContext, MainActivity::class.java)
    intent.apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }
    val pi = PendingIntent.getActivity(context, 0, intent, FLAG_UPDATE_CURRENT)
    val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Notification.Builder(context.applicationContext,
            context.getString(R.string.notification_channel_intent_id))
    } else {
        Notification.Builder(context.applicationContext)
    }
    builder.apply {
        setContentTitle(context.getString(R.string.notification_intent_title))
        setContentText(context.getString(R.string.notification_intent_content))
        setWhen(System.currentTimeMillis())
        setSmallIcon(R.drawable.ic_notify_small)
        setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round))
        setContentIntent(pi)
        setAutoCancel(true)
    }
    return builder.build()
}
