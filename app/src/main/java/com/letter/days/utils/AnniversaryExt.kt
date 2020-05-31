package com.letter.days.utils

import com.haibin.calendarview.Calendar
import com.haibin.calendarview.LunarUtil
import com.letter.days.database.entity.AnniversaryEntity
import com.letter.days.database.entity.AnniversaryEntity.Companion.ANNI_TYPE_COUNT_DOWN
import com.letter.days.database.entity.AnniversaryEntity.Companion.ANNI_TYPE_EVERY_YEAR
import com.letter.days.database.entity.AnniversaryEntity.Companion.ANNI_TYPE_ONLY_ONCE
import com.letter.days.database.entity.AnniversaryEntity.Companion.ANNI_TYPE_TEXT

typealias JCalendar = java.util.Calendar

/**
 * 纪念日扩展
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */

/**
 * 获取纪念日日期字符串文本
 * @param format String 日期格式
 * @param lunarFormat String 农历日期格式
 * @return String? 日期字符串文本
 */
fun AnniversaryEntity.getDateString(format: String = "%s/%s/%s",
                                    lunarFormat: String = "%s %s %s"): String {
    val jCalendar = JCalendar.getInstance()
    jCalendar.timeInMillis = time
    val year = jCalendar.get(JCalendar.YEAR)
    val month = jCalendar.get(JCalendar.MONTH) + 1
    val day = jCalendar.get(JCalendar.DAY_OF_MONTH)

    return if (lunar) {
        val lunarValue = LunarUtil.solarToLunar(year, month, day);
        lunarFormat.format(
            lunarValue[0].toString(),
            getLunarMonthText(lunarValue[1], lunarValue[3]),
            getLunarDayText(lunarValue[2]))
    } else {
        format.format(year.toString(), month.toString(), day.toString())
    }
}

/**
 * 获取类型文本
 * @receiver AnniversaryEntity 纪念日
 * @return String 类型文本
 */
fun AnniversaryEntity.getTypeText(): String =
    when (type) {
        ANNI_TYPE_ONLY_ONCE, ANNI_TYPE_COUNT_DOWN -> ANNI_TYPE_TEXT[type]
        ANNI_TYPE_EVERY_YEAR -> {
            val distance = getDistance(AnniversaryEntity.DistanceMode.DISTANCE_NEXT)
            if (distance > 0) {
                "${ANNI_TYPE_TEXT[ANNI_TYPE_EVERY_YEAR]} · ${distance}天"
            } else if (distance == 0) {
                "${ANNI_TYPE_TEXT[ANNI_TYPE_EVERY_YEAR]} · 今天"
            } else {
                ANNI_TYPE_TEXT[ANNI_TYPE_EVERY_YEAR]
            }
        }
        else -> ""
    }

/**
 * 获取天数文本
 * @receiver AnniversaryEntity 纪念日
 * @return String 天数文本
 */
fun AnniversaryEntity.getDayText() = run {
    val distance = getDistance(AnniversaryEntity.DistanceMode.DISTANCE_ABS)
    if (distance > 0) {
        "${distance}天"
    } else if (distance == 0) {
        "今天"
    } else {
        "差${-distance}天"
    }
}

/**
 * 获取纪念日下一次时间
 * @receiver AnniversaryEntity 纪念日
 * @return Int 下一次时间
 */
fun AnniversaryEntity.getNextTime() =
    when (type) {
        ANNI_TYPE_EVERY_YEAR -> getDistance(AnniversaryEntity.DistanceMode.DISTANCE_NEXT)
        ANNI_TYPE_COUNT_DOWN -> {
            val nextTime = -getDistance(AnniversaryEntity.DistanceMode.DISTANCE_ABS)
            if (nextTime < 0) -1 else nextTime
        }
        else -> -1
    }

/**
 * 获取纪念日到当前日期的距离
 * @receiver AnniversaryEntity 纪念日
 * @param mode DistanceMode 距离模式
 * @return Int 距离
 */
fun AnniversaryEntity.getDistance(mode: AnniversaryEntity.DistanceMode): Int {
    val nowCalendar = Calendar().setTimeInMillis()
    val anniCalendar = Calendar().setTimeInMillis(time)

    return when (type) {
        ANNI_TYPE_ONLY_ONCE, ANNI_TYPE_COUNT_DOWN -> nowCalendar.differ(anniCalendar)
        ANNI_TYPE_EVERY_YEAR -> {
            if (mode == AnniversaryEntity.DistanceMode.DISTANCE_ABS)
                nowCalendar.differ(anniCalendar)
            else
                getNextCalendar().differ(nowCalendar)
        }
        else -> 0
    }
}

/**
 * 获取纪念日的下一个日期
 * @receiver AnniversaryEntity 纪念日
 * @return Calendar 日历
 */
fun AnniversaryEntity.getNextCalendar(): Calendar {
    val nowCalendar = Calendar().setTimeInMillis()
    val anniCalendar = Calendar().setTimeInMillis(time)

    return when (type) {
        ANNI_TYPE_EVERY_YEAR -> {
            val tmpCalendar = Calendar()
            if (lunar) {
                val anniDate = LunarUtil.solarToLunar(anniCalendar.year, anniCalendar.month, anniCalendar.day)
                val nowDate = LunarUtil.solarToLunar(nowCalendar.year, nowCalendar.month, nowCalendar.day)
                val nextDate = intArrayOf(nowDate[0], anniDate[1], anniDate[2], anniDate[3])
                if ((nextDate[0] * 10000 + nextDate[1] * 100 + nextDate[2]) <
                    (nowDate[0] * 1000 + nowDate[1] * 100 + nowDate[2])) {
                    nextDate[0] += 1
                }
                val nextSolarDate = LunarUtil.lunarToSolar(nextDate[0], nextDate[1], nextDate[2], nextDate[3] == 1)
                tmpCalendar.year = nextSolarDate[0]
                tmpCalendar.month = nextSolarDate[1]
                tmpCalendar.day = nextSolarDate[2]
            } else {
                tmpCalendar.year = nowCalendar.year
                tmpCalendar.month = anniCalendar.month
                tmpCalendar.day = anniCalendar.day

                if (tmpCalendar.differ(nowCalendar) < 0) {
                    tmpCalendar.year += 1
                }
            }

            tmpCalendar
        }
        else-> anniCalendar
    }
}
