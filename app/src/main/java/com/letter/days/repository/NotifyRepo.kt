package com.letter.days.repository

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.letter.days.R
import com.letter.days.data.bean.NotifyBean
import com.letter.days.data.bean.hour
import com.letter.days.data.bean.minute
import com.letter.days.service.CoreService
import com.letter.days.utils.JCalendar
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.*

private const val TAG = "NotifyRepo"

class NotifyRepo {

    suspend fun requestNotify(url: String?): String? {
        if (TextUtils.isEmpty(url)) {
            return null
        }
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url!!)
            .build()
        var response: Response? = null
        try {
            response = client.newCall(request).execute()
            return if (response.code == 200) {
                response.body?.string()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "", e)
        } finally {
            response?.close()
        }
        return null
    }

    @Synchronized
    suspend fun saveNotifyBean(context: Context, content: String) {
        var out: FileOutputStream? = null
        var writer: BufferedWriter? = null
        try {
            out = context.openFileOutput(NOTIFY_BEAN_FILE, Context.MODE_PRIVATE)
            writer = BufferedWriter(OutputStreamWriter(out))
            writer.write(content)
        } catch (e: IOException) {

        } finally {
            writer?.flush()
            out?.flush()
            writer?.close()
            out?.close()
        }
    }

    suspend fun readNotifyBean(context: Context): String {
        var input: FileInputStream? = null
        var reader: BufferedReader? = null
        val content = StringBuilder()
        try {
            input = context.openFileInput(NOTIFY_BEAN_FILE)
            reader = BufferedReader(InputStreamReader(input))
            var line: String? = reader.readLine()
            while (line != null) {
                content.append(line)
                line = reader.readLine()
            }
        } catch (e: IOException) {

        } finally {
            reader?.close()
            input?.close()
        }
        return content.toString()
    }

    suspend fun getNotifyBean(context: Context, url: String?): NotifyBean? {
        var content = requestNotify(url)
        Log.d(TAG, "getNotifyBean: $content")
        if (content != null) {
            saveNotifyBean(context, content)
        } else {
            content = readNotifyBean(context)
        }
        if (TextUtils.isEmpty(content)) {
            return null
        }
        return Gson().fromJson(content, NotifyBean::class.java)
    }

    suspend fun getNotifyBeanSorted(context: Context, url: String?): NotifyBean? {
        val notifyBean = getNotifyBean(context, url)
        notifyBean?.notify = notifyBean?.notify?.sortedWith(compareBy({it.hour}, {it.minute}))?.reversed()
        return notifyBean
    }

    suspend fun setNotifyAlarm(context: Context) {
        var notifyItemBean: NotifyBean.NotifyItemBean? = null
        val notifyBean = getNotifyBeanSorted(context,
            PreferenceManager.getDefaultSharedPreferences(context).getString("notify_source", null))
        val notifyCalendar = JCalendar.getInstance()

        try {
            for (i in 0 until (notifyBean?.notify?.size ?: 0)) {
                notifyCalendar.apply {
                    set(JCalendar.HOUR_OF_DAY, notifyBean?.notify?.get(i)!!.hour)
                    set(JCalendar.MINUTE, notifyBean?.notify?.get(i)!!.minute)
                    set(JCalendar.SECOND, 0)
                    set(JCalendar.MILLISECOND, 0)
                }
                if (notifyCalendar.timeInMillis > JCalendar.getInstance().timeInMillis) {
                    Log.d(TAG, "notify: ${notifyCalendar.timeInMillis}, time: ${JCalendar.getInstance().timeInMillis}")
                    notifyItemBean = notifyBean?.notify?.get(i)
                    break
                }
            }
            if (notifyItemBean == null && notifyBean?.notify?.size ?: 0 > 0) {
                notifyItemBean = notifyBean?.notify?.get(0)
                notifyCalendar.apply {
                    set(JCalendar.HOUR_OF_DAY, notifyBean?.notify?.get(0)?.hour ?: 0)
                    set(JCalendar.MINUTE, notifyBean?.notify?.get(0)?.minute ?: 0)
                    set(JCalendar.SECOND, 0)
                    set(JCalendar.MILLISECOND, 0)
                }
                notifyCalendar.timeInMillis = notifyCalendar.timeInMillis + 24 * 60 * 60 * 1000
            }

            val intent = Intent(context, CoreService::class.java)
            intent.apply {
                putExtra(CoreService.EXTRA_IS_TIMER_NOTIFY, true)
                putExtra(CoreService.EXTRA_NOTIFY_TITLE, notifyItemBean?.title)
                putExtra(CoreService.EXTRA_NOTIFY_CONTENT,
                    notifyItemBean?.content?.get((0 until (notifyItemBean?.content?.size ?: 0)).random()))
            }
            val pi = PendingIntent.getService(context, -2, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?)?.set(
                AlarmManager.RTC_WAKEUP,
                notifyCalendar.timeInMillis,
                pi
            )
        } catch (e: Exception) {
            Log.e(TAG, "", e)
        }
    }

    fun showNotification(context: Context, title: String?, content: String?) {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context.applicationContext,
                context.getString(R.string.notification_channel_anni_id))
        } else {
            Notification.Builder(context.applicationContext)
        }
        builder.apply {
            setContentTitle(title)
            setContentText(content)
            setWhen(System.currentTimeMillis())
            setSmallIcon(R.drawable.ic_notify_small)
            setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round))
            setAutoCancel(true)
        }
        val notification = builder.build()
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)?.notify(-3, notification)
    }

    companion object {
        private const val NOTIFY_BEAN_FILE = "notify_bean.json"
    }
}