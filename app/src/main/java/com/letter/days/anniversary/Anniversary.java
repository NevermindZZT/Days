package com.letter.days.anniversary;

import org.litepal.crud.LitePalSupport;

import java.util.Calendar;

/**
 * @brief 纪念日
 */
public class Anniversary extends LitePalSupport {
    public static final int ANNI_TYPE_ONLY_ONCE = 0;
    public static final int ANNI_TYPE_EVERY_YEAR = 1;
    public static final int ANNI_TYPE_COUNT_DOWN = 2;

    private static final long MS_ONE_DAY = 86400000L;

    public static final String[] typeText = {
            "纪念日",
            "周年纪念",
            "倒计时",
    };

    private int id;
    private long time;
    private String text;
    private int type;

    public Anniversary() {
        this.time = (Calendar.getInstance().getTimeInMillis() / MS_ONE_DAY) * MS_ONE_DAY;
        this.text = "";
        this.type = ANNI_TYPE_ONLY_ONCE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDaysText() {
        String text = new String();
        Calendar now = Calendar.getInstance();
        AnniUtils.setTimeToZero(now);
        switch (type) {
            case ANNI_TYPE_ONLY_ONCE:
                if (time <= now.getTimeInMillis()) {
                    text = String.valueOf((now.getTimeInMillis() - time) / MS_ONE_DAY) + "天";
                } else {
                    text = "差" + String.valueOf((time - now.getTimeInMillis()) / MS_ONE_DAY) + "天";
                }
                break;

            case ANNI_TYPE_EVERY_YEAR:
                if (time <= now.getTimeInMillis()) {
                    text = String.valueOf((now.getTimeInMillis() - time) / MS_ONE_DAY) + "天";
                } else {
                    text = "差" + String.valueOf((time - now.getTimeInMillis()) / MS_ONE_DAY) + "天";
                }
                break;

            case ANNI_TYPE_COUNT_DOWN:
                if (time < now.getTimeInMillis()) {
                    text = "已过" + String.valueOf((now.getTimeInMillis() - time) / MS_ONE_DAY) + "天";
                } else if (time == now.getTimeInMillis()) {
                    text = "今天";
                } else {
                    text = "余" + String.valueOf((time - now.getTimeInMillis()) / MS_ONE_DAY) + "天";
                }
                break;

            default:
                break;
        }
        return text;
    }

    public String getDateText () {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return String.valueOf(calendar.get(Calendar.YEAR) + "-"
                + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "-"
                + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
    }

    public String getTypeText () {
        String text = new String();
        Calendar now = Calendar.getInstance();
        AnniUtils.setTimeToZero(now);
        switch (type) {
            case ANNI_TYPE_ONLY_ONCE:
                text = typeText[type];
                break;

            case ANNI_TYPE_EVERY_YEAR:
                if (time <= now.getTimeInMillis()) {
                    Calendar anniCalender = Calendar.getInstance();
                    anniCalender.setTimeInMillis(time);
                    int nowYear = now.get(Calendar.YEAR);

                    int month = anniCalender.get(Calendar.MONTH);
                    int day = anniCalender.get(Calendar.DAY_OF_MONTH);

                    Calendar tmpCalender = Calendar.getInstance();
                    AnniUtils.setTimeToZero(tmpCalender);
                    tmpCalender.set(nowYear, month, day);

                    if (tmpCalender.getTimeInMillis() - now.getTimeInMillis() > 0) {
                        text = typeText[type] + " · "
                                + String.valueOf((tmpCalender.getTimeInMillis() - now.getTimeInMillis()) / MS_ONE_DAY) + "天";
                    } else if (tmpCalender.getTimeInMillis() - now.getTimeInMillis() == 0) {
                        text = typeText[type] + " · 今天";
                    } else {
                        tmpCalender.set(Calendar.YEAR, nowYear + 1);
                        text = typeText[type] + " · "
                                + String.valueOf((tmpCalender.getTimeInMillis() - now.getTimeInMillis()) / MS_ONE_DAY) + "天";
                    }
                } else {
                    text = typeText[type];
                }
                break;

            case ANNI_TYPE_COUNT_DOWN:
                text = typeText[type];
                break;

            default:
                break;
        }
        return text;
    }

    public long getNextTime () {
        long nextTime = -1;
        Calendar now = Calendar.getInstance();
        AnniUtils.setTimeToZero(now);
        switch (type) {
            case ANNI_TYPE_ONLY_ONCE:
                nextTime = -1;
                break;

            case ANNI_TYPE_EVERY_YEAR:
                if (time <= now.getTimeInMillis()) {
                    Calendar anniCalender = Calendar.getInstance();
                    anniCalender.setTimeInMillis(time);
                    int nowYear = now.get(Calendar.YEAR);

                    int month = anniCalender.get(Calendar.MONTH);
                    int day = anniCalender.get(Calendar.DAY_OF_MONTH);

                    Calendar tmpCalender = Calendar.getInstance();
                    AnniUtils.setTimeToZero(tmpCalender);
                    tmpCalender.set(nowYear, month, day);

                    if (tmpCalender.getTimeInMillis() - now.getTimeInMillis() >= 0) {
                        nextTime = (tmpCalender.getTimeInMillis() - now.getTimeInMillis()) / MS_ONE_DAY;
                    } else {
                        tmpCalender.set(Calendar.YEAR, nowYear + 1);
                        nextTime = (tmpCalender.getTimeInMillis() - now.getTimeInMillis()) / MS_ONE_DAY;
                    }
                } else {
                    nextTime = -1;
                }
                break;

            case ANNI_TYPE_COUNT_DOWN:
                if (time >= now.getTimeInMillis()) {
                    nextTime = (time - now.getTimeInMillis()) / MS_ONE_DAY;
                } else {
                    nextTime = -1;
                }
                break;

            default:
                break;

        }
        return nextTime;
    }
}
