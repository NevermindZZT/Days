package com.letter.days.utils

import com.haibin.calendarview.Calendar


/**
 * 日历扩展
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */


fun Calendar.setTimeInMillis(millis: Long? = null): Calendar {
    val jCalendar = JCalendar.getInstance()
    if (millis != null) {
        jCalendar.timeInMillis = millis
    }

    year = jCalendar.get(JCalendar.YEAR)
    month = jCalendar.get(JCalendar.MONTH) + 1
    day = jCalendar.get(JCalendar.DAY_OF_MONTH)

    return this
}

