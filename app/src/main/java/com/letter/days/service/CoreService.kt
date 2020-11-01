package com.letter.days.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.letter.days.R
import com.letter.days.database.AppDatabase
import com.letter.days.utils.setAllAnniversaryAlarm
import com.letter.days.utils.showAnniversaryNotification
import android.content.createNotificationChannel
import android.text.TextUtils
import androidx.preference.PreferenceManager
import com.letter.days.repository.NotifyRepo
import com.letter.days.utils.getIntentNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 核心服务
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class CoreService : Service() {

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                getString(R.string.notification_channel_anni_id),
                getString(R.string.notification_channel_anni_name),
                NotificationManager.IMPORTANCE_HIGH)
            createNotificationChannel(
                getString(R.string.notification_channel_intent_id),
                getString(R.string.notification_channel_intent_name),
                NotificationManager.IMPORTANCE_MIN)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("intent_notification", false)) {
            startForeground(-1, getIntentNotification(this))
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(-1)
            } else {
                stopForeground(true)
            }
        }
        if (intent?.getBooleanExtra(EXTRA_IS_DAYS_NOTIFY, false) == true) {
            val anniId = intent.getIntExtra(EXTRA_ANNI_ID, -1)
            if (anniId != -1) {
                GlobalScope.launch {
                    val anniversary = AppDatabase.instance(this@CoreService)
                        .anniversaryDao()
                        .get(anniId)
                    if (anniversary != null) {
                        withContext(Dispatchers.Main) {
                            showAnniversaryNotification(this@CoreService, anniversary)
                        }
                    }
                }
            }
        }
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("enable_notify", false)) {
            GlobalScope.launch {
                NotifyRepo().setNotifyAlarm(this@CoreService)
            }
        }
        if (intent?.getBooleanExtra(EXTRA_IS_TIMER_NOTIFY, false) == true) {
            val title = intent.getStringExtra(EXTRA_NOTIFY_TITLE)
            if (!TextUtils.isEmpty(title)) {
                NotifyRepo().showNotification(this,
                    title,
                    intent.getStringExtra(EXTRA_NOTIFY_CONTENT))
            }
        }
        GlobalScope.launch {
            setAllAnniversaryAlarm(this@CoreService)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        const val EXTRA_IS_DAYS_NOTIFY = "is_days_notify"
        const val EXTRA_ANNI_ID = "anni_id"
        const val EXTRA_IS_TIMER_NOTIFY = "is_timer_notify"
        const val EXTRA_NOTIFY_TITLE = "notify_title"
        const val EXTRA_NOTIFY_CONTENT = "notify_content"
    }
}
