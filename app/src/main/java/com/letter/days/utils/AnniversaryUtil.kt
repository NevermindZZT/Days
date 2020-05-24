package com.letter.days.utils

import com.haibin.calendarview.Calendar
import com.haibin.calendarview.LunarUtil
import com.letter.days.database.entity.AnniversaryEntity

typealias JCalendar = java.util.Calendar

/**
 * 纪念日工具
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */

/**
 * 获取纪念日日期字符串文本
 * @param anniversary AnniversaryEntity? 纪念日
 * @param format String 日期格式
 * @return String? 日期字符串文本
 */
fun getDateString(anniversary: AnniversaryEntity?, format: String = "%s/%s/%s"): String? {
    if (anniversary == null) {
        return null;
    }

    val jCalendar = JCalendar.getInstance()
    jCalendar.timeInMillis = anniversary.time
    val year = jCalendar.get(JCalendar.YEAR)
    val month = jCalendar.get(JCalendar.MONTH) + 1
    val day = jCalendar.get(JCalendar.DAY_OF_MONTH)

    if (anniversary.lunar) {
        val lunarValue = LunarUtil.solarToLunar(year, month, day);
        return format.format(
            lunarValue[0].toString(),
            getLunarMonthText(lunarValue[1], lunarValue[3]),
            getLunarDayText(lunarValue[2]))
    } else {
        return format.format(year.toString(), month.toString(), day.toString())
    }
}