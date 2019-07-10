package com.letter.days.anniversary;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.LunarUtil;

import org.litepal.crud.LitePalSupport;


/**
 * 纪念日
 */
public class Anniversary extends LitePalSupport {
    public static final int ANNI_TYPE_ONLY_ONCE = 0;
    public static final int ANNI_TYPE_EVERY_YEAR = 1;
    public static final int ANNI_TYPE_COUNT_DOWN = 2;

    private static final int DISTANCE_NEXT = 0;
    private static final int DISTANCE_ABS = 1;

    public static final String[] typeText = {
            "纪念日",
            "周年纪念",
            "倒计时",
    };

    private int id;
    private long time;
    private String text;
    private int type;
    private boolean lunar;

    public Anniversary() {
        this.time = AnniUtils.getCalendar(java.util.Calendar.getInstance().getTimeInMillis()).getTimeInMillis();
        this.text = "";
        this.type = ANNI_TYPE_ONLY_ONCE;
        this.lunar = false;
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

    public boolean isLunar() {
        return lunar;
    }

    public void setLunar(boolean lunar) {
        this.lunar = lunar;
    }

    /**
     * 获取下一次纪念日日期
     * @return 日期
     */
    public Calendar getNextCalendar() {
        Calendar now = AnniUtils.getCalendar(java.util.Calendar.getInstance().getTimeInMillis());
        Calendar anni = AnniUtils.getCalendar(time);
        Calendar calendar = null;
        switch (type) {
            case ANNI_TYPE_ONLY_ONCE:
                calendar = anni;
                break;
            case ANNI_TYPE_EVERY_YEAR:
                Calendar tmpCalender = new Calendar();
                if (lunar) {
                    int[] lunarDate = LunarUtil.solarToLunar(anni.getYear(), anni.getMonth(), anni.getDay());
                    int[] nowLunarDate = LunarUtil.solarToLunar(now.getYear(), now.getMonth(), now.getDay());
                    int[] nextLunarDate = {nowLunarDate[0], lunarDate[1], lunarDate[2], lunarDate[3]};
                    if ((nextLunarDate[0] * 10000 + nextLunarDate[1] * 100 + nextLunarDate[2]) <
                            (nowLunarDate[0] * 10000 + nowLunarDate[1]*100 + nextLunarDate[2])) {
                        nextLunarDate[0] += 1;
                    }
                    int[] nextSolarDate = LunarUtil.lunarToSolar(nextLunarDate[0], nextLunarDate[1],
                            nextLunarDate[2], nextLunarDate[3] == 1);
                    tmpCalender.setYear(nextSolarDate[0]);
                    tmpCalender.setMonth(nextSolarDate[1]);
                    tmpCalender.setDay(nextSolarDate[2]);

                    calendar = tmpCalender;
                } else {
                    tmpCalender.setYear(now.getYear());
                    tmpCalender.setMonth(anni.getMonth());
                    tmpCalender.setDay(anni.getDay());

                    if (tmpCalender.differ(now) < 0) {
                        tmpCalender.setYear(now.getYear() + 1);
                    }
                    calendar = tmpCalender;
                }
                break;
            case ANNI_TYPE_COUNT_DOWN:
                calendar = anni;
                break;
        }
        return calendar;
    }

    /**
     * 获得日期到当前时间的距离
     * @param distanceType 每年循环或者绝对距离
     * @return 日期距离
     */
    private int getDistance(int distanceType) {
        Calendar now = AnniUtils.getCalendar(java.util.Calendar.getInstance().getTimeInMillis());
        Calendar anni = AnniUtils.getCalendar(time);
        int distance = 0;
        switch (type) {
            case ANNI_TYPE_ONLY_ONCE:
                distance = now.differ(anni);
                break;
            case ANNI_TYPE_EVERY_YEAR:
                if (distanceType == DISTANCE_ABS) {
                    distance = now.differ(anni);
                } else {
                    distance = getNextCalendar().differ(now);
                }
                break;
            case ANNI_TYPE_COUNT_DOWN:
                distance = now.differ(anni);
                break;
            default:
                break;
        }
        return distance;
    }

    /**
     * 获取绝对日期文本
     * @return 文本
     */
    public String getDaysText() {
        String text;
        int day = getDistance(DISTANCE_ABS);
        if (day > 0) {
            text = day + "天";
        } else if (day == 0) {
            text = "今天";
        } else {
            text = "差" + (-day) + "天";
        }
        return text;
    }

    /**
     * 获取格式化日期文本
     * @return 日期文本
     */
    public String getDateText () {
        return AnniUtils.getFormatDate("%d-%d-%d", "%d %s %s", time, lunar);
    }

    /**
     * 获取格式化日期文本
     * @param format 公历格式
     * @param lunarFormat 农历格式
     * @return 日期文本
     */
    public String getDateText(String format, String lunarFormat) {
        return AnniUtils.getFormatDate(format, lunarFormat, time, lunar);
    }

    /**
     * 获取类型文本
     * @return 类型文本
     */
    public String getTypeText () {
        String text = "";
        switch (type) {
            case ANNI_TYPE_ONLY_ONCE:
                text = typeText[type];
                break;

            case ANNI_TYPE_EVERY_YEAR:
                int day = getDistance(DISTANCE_NEXT);
                if (day > 0) {
                    text = typeText[type] + " · " + day + "天";
                } else if (day == 0) {
                    text = typeText[type] + " · 今天";
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

    /**
     * 获取纪念日的下一次时间
     * @return 时间
     */
    public long getNextTime () {
        long nextTime = -1;
        switch (type) {
            case ANNI_TYPE_ONLY_ONCE:
                nextTime = -1;
                break;

            case ANNI_TYPE_EVERY_YEAR:
                nextTime = getDistance(DISTANCE_NEXT);
                break;

            case ANNI_TYPE_COUNT_DOWN:
                if ((nextTime = getDistance(DISTANCE_ABS)) < 0) {
                    nextTime = -1;
                }
                break;

            default:
                break;

        }
        return nextTime;
    }
}
