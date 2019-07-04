package com.letter.days.anniversary;

import android.app.ActivityManager;
import android.content.Context;

import com.haibin.calendarview.LunarUtil;
import com.letter.days.R;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class AnniUtils {

    private static String[] LUNAR_MONTH = {
            "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "腊月"
    };
    private static String[] LUNAR_DAY = {
            "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
            "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
            "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"
    };

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

    public static void setCalendarTime(com.haibin.calendarview.Calendar calendar, long time) {
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTimeInMillis(time);

        calendar.setYear(calendarTime.get(Calendar.YEAR));
        calendar.setMonth(calendarTime.get(Calendar.MONTH) + 1);
        calendar.setDay(calendarTime.get(Calendar.DAY_OF_MONTH));
    }

    public static com.haibin.calendarview.Calendar getCalendar(long time) {
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTimeInMillis(time);

        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        calendar.setYear(calendarTime.get(Calendar.YEAR));
        calendar.setMonth(calendarTime.get(Calendar.MONTH) + 1);
        calendar.setDay(calendarTime.get(Calendar.DAY_OF_MONTH));
        return calendar;
    }

    public static String getFormatDate(String format, String lunarFormat,
                                       com.haibin.calendarview.Calendar calendar, boolean isLeap) {
        if (!isLeap) {
            return String.format(format, calendar.getYear(),
                    calendar.getMonth(), calendar.getDay());
        } else {
            int[] lunar = LunarUtil.solarToLunar(calendar.getYear(), calendar.getMonth(), calendar.getDay());
            return String.format(lunarFormat,
                    lunar[0],
                    AnniUtils.getLunarMonthText(lunar[1], lunar[3]),
                    AnniUtils.getLunarDayText(lunar[2]));
        }
    }

    public static String getFormatDate(String format, String lunarFormat, long time, boolean isLeap) {
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        AnniUtils.setCalendarTime(calendar, time);
        return getFormatDate(format, lunarFormat, calendar, isLeap);
    }

    public static String getLunarMonthText(int month, int isLeap) {
        if (isLeap == 1) {
            return "闰" + LUNAR_MONTH[month - 1];
        } else {
            return LUNAR_MONTH[month - 1];
        }
    }

    public static String getLunarDayText(int day) {
        return LUNAR_DAY[day - 1];
    }
}
