package com.letter.days.utils

/**
 * 农历工具
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */

/**
 * 农历月份
 */
private val LUNAR_MONTH =
    arrayOf(
        "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "腊月")

/**
 * 农历天
 */
private val LUNAR_DAY =
    arrayOf(
        "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
        "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
        "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十")

/**
 * 获取农历月份文本
 * @param month Int 月份
 * @param isLeap Int 是否闰月
 * @return String 农历月份文本
 */
fun getLunarMonthText(month: Int, isLeap: Int) =
    if (isLeap == 1) "闰%d".format(LUNAR_MONTH[month - 1]) else LUNAR_MONTH[month - 1]

/**
 * 获取农历天文本
 * @param day Int 天
 * @return String 农历天文本
 */
fun getLunarDayText(day: Int) = LUNAR_DAY[day - 1]
