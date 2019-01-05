package com.letter.days.anniversary;

import android.app.ActivityManager;
import android.content.Context;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class AnniUtils {
    public static Anniversary getClosestAnni () {
        Anniversary closestAnni;
        List<Anniversary> anniversaryList = LitePal.findAll(Anniversary.class);
        if (anniversaryList.size() == 0) {
            return new Anniversary();
        }
        closestAnni = anniversaryList.get(0);
        for (Anniversary anni : anniversaryList) {
            if ((anni.getNextTime() >= 0) &&
                    ((anni.getNextTime() < closestAnni.getNextTime()) || (closestAnni.getNextTime() < 0))) {
                closestAnni = anni;
            }
        }
        return closestAnni;
    }

    public static List<Anniversary> getNotifyAnni () {
        List<Anniversary> notifyAnni = new LinkedList<>();
        List<Anniversary> anniversaryList = LitePal.findAll(Anniversary.class);
        if (anniversaryList == null) {
            return null;
        } else {
            for (Anniversary anni : anniversaryList) {
                if (anni.getNextTime() == 0) {
                    notifyAnni.add(anni);
                }
            }
        }
        return notifyAnni;
    }

    public static boolean isNotifyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.letter.otools.service.NotifyService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void setTimeToZero (Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
