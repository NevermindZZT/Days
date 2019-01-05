package com.letter.days.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.letter.days.R;
import com.letter.days.activity.AnniversaryActivity;
import com.letter.days.anniversary.AnniUtils;
import com.letter.days.anniversary.Anniversary;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class NotifyService extends Service {
    public NotifyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO: 通知
                Log.d("Notify", "run notify");
                List<Anniversary> anniversaryList = AnniUtils.getNotifyAnni();
                if (anniversaryList != null) {
                    Log.d("Notify", "some days to notify");
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    int id = 0;
                    for (Anniversary anni : anniversaryList) {
                        Intent intentItem =  new Intent(getApplicationContext(), AnniversaryActivity.class);
                        intentItem.putExtra("anniId", anni.getId());
                        intentItem.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), new Random().nextInt(), intentItem, PendingIntent.FLAG_UPDATE_CURRENT);


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Notification notification = new Notification.Builder(getApplicationContext(), "anni")
                                    .setContentTitle(anni.getTypeText())
                                    .setContentText(anni.getText() + "  日子到了哦")
                                    .setWhen(System.currentTimeMillis())
                                    .setSmallIcon(R.drawable.ic_notify)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon))
                                    .setContentIntent(pi)
                                    .setAutoCancel(true)
                                    .build();

                            manager.notify(++id, notification);
                            Log.d("Notify", anni.getText() + " - " + anni.getTypeText());
                        } else {
                            Notification notification = new Notification.Builder(getApplicationContext())
                                    .setContentTitle(anni.getTypeText())
                                    .setContentText(anni.getText() + "  日子到了哦")
                                    .setWhen(System.currentTimeMillis())
                                    .setSmallIcon(R.drawable.ic_notify)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon))
                                    .setContentIntent(pi)
                                    .setAutoCancel(true)
                                    .build();

                            manager.notify(++id, notification);
                            Log.d("Notify", anni.getText() + " - " + anni.getTypeText());
                        }
                    }
                }
            }
        }).start();

        Log.d("Notify", "onStartCommand: service start");
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        Log.d("Time", "onStartCommand: " + String.valueOf(calendar.get(Calendar.YEAR)) + "-"
                + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "-"
                + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " "
                + String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                + String.valueOf(calendar.get(Calendar.MINUTE)));
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 9) {
            calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 10);
        calendar.set(Calendar.MILLISECOND, 0);
        Log.d("Alarm", "onStartCommand: " + String.valueOf(calendar.get(Calendar.YEAR)) + "-"
                + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "-"
                + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " "
                + String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                + String.valueOf(calendar.get(Calendar.MINUTE)));

        Intent intent1 = new Intent(getApplicationContext(), NotifyService.class);
        PendingIntent pi = PendingIntent.getService(getApplicationContext(), 0, intent1, 0);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

        return super.onStartCommand(intent, flags, startId);
    }
}
