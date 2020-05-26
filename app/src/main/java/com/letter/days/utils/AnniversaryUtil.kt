package com.letter.days.utils

import android.graphics.Color
import android.graphics.drawable.*
import android.view.Gravity
import com.letter.days.database.entity.AnniversaryEntity

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
 * @param lunarFormat String 农历日期格式
 * @return String? 日期字符串文本
 */
fun getDateString(anniversary: AnniversaryEntity?,
                  format: String = "%s/%s/%s",
                  lunarFormat: String = "%s %s %s") =
    anniversary?.getDateString(format, lunarFormat)

/**
 * 纪念日获取类型文本
 * @param anniversary AnniversaryEntity? 纪念日
 * @return String? 类型文本
 */
fun getTypeText(anniversary: AnniversaryEntity?) =
    anniversary?.getTypeText()

/**
 * 获取纪念日天数文本
 * @param anniversary AnniversaryEntity? 纪念日
 * @return String? 天数文本
 */
fun getDayText(anniversary: AnniversaryEntity?) =
    anniversary?.getDayText()

/**
 * 获取纪念日下一次时间
 * @param anniversary AnniversaryEntity? 纪念日
 * @return Int? 下一次时间
 */
fun getNextTime(anniversary: AnniversaryEntity?) =
    anniversary?.getNextTime()

fun getProgressDrawable(anniversary: AnniversaryEntity?): Drawable? {
    if (anniversary == null) {
        return null
    }

    val backDrawable = ColorDrawable(Color.TRANSPARENT)

    val gradientDrawable = GradientDrawable()
    gradientDrawable.setColor(anniversary.color)
    val clipDrawable = ClipDrawable(gradientDrawable, Gravity.START, ClipDrawable.HORIZONTAL)

    val drawable = LayerDrawable(arrayOf(backDrawable, clipDrawable))

    drawable.setId(0, android.R.id.background)
    drawable.setId(1, android.R.id.progress)

    return drawable
}
