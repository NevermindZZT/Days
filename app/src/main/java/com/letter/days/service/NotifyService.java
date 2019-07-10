package com.letter.days.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import com.letter.days.R;
import com.letter.days.activity.AnniversaryActivity;
import com.letter.days.anniversary.AnniUtils;
import com.letter.days.anniversary.Anniversary;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.List;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class NotifyService extends Service {

//    private static final String TAG = "NotifyService";

    private static final long MILLIS_AN_HOUR = 60 * 60 * 1000;

    private static final String INTENT_KEY = "type";
    private static final String INTENT_ID = "id";

    private static final int TYPE_NONE = 0;
    private static final int TYPE_NOTIFY = 1;

    public NotifyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        String channelId = "anni";
        String channelName = "纪念日提醒";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        createNotificationChannel(channelId, channelName, importance);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getIntExtra(INTENT_KEY, TYPE_NONE) == TYPE_NOTIFY) {
            showNotification(intent.getIntExtra(INTENT_ID, 0));
        }
        setAlarm();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 设置通知闹钟
     */
    private void setAlarm() {
        List<Anniversary> anniversaryList = LitePal.findAll(Anniversary.class);
        int requestCode = 0;
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        if (alarmManager == null) {
            return;
        }
        for (Anniversary anniversary: anniversaryList) {
            long time = anniversary.getNextCalendar().getTimeInMillis();
            time = AnniUtils.setTimeToZero(time) + 9 * MILLIS_AN_HOUR;
            if (time > Calendar.getInstance().getTimeInMillis()) {
                Intent intent = new Intent(this, NotifyService.class);
                intent.putExtra(INTENT_KEY, TYPE_NOTIFY);
                intent.putExtra(INTENT_ID, anniversary.getId());
                PendingIntent pi = PendingIntent.getService(this, requestCode, intent, FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, pi);
            }
            requestCode ++;
        }
    }

    /**
     * 显示通知
     * @param anniId 纪念日ID
     */
    private void showNotification(int anniId) {
        Anniversary anniversary = LitePal.find(Anniversary.class, anniId);
        if (anniversary == null) {
            return;
        }
        Intent intentItem =  new Intent(getApplicationContext(), AnniversaryActivity.class);
        intentItem.putExtra("anniId", anniId);
        intentItem.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, anniId, intentItem,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(getApplicationContext(), "anni")
                .setContentTitle(anniversary.getTypeText())
                .setContentText(anniversary.getText() + "  日子到了哦")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notify)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager == null) {
            return;
        }
        manager.notify(anniId, notification);
    }

    /**
     * 创建通知渠道
     * @param channelId id
     * @param channelName 名称
     * @param importance 重要性
     */
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        notificationManager.createNotificationChannel(channel);
    }

}
