package com.letter.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Context Utils
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */

/**
 * Toast
 * @receiver Context
 * @param message String 消息
 * @param duration Int 提示时长
 */
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT)
        = Toast.makeText(this, message, duration).show()

/**
 * Toast
 * @receiver Context
 * @param resId Int 文本资源id
 * @param duration Int 提示时长
 */
fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT)
        = Toast.makeText(this, resources.getText(resId), duration).show()

/**
 * 判断是否为暗黑模式
 * @receiver Context context
 * @return Boolean {@code true}暗黑模式 {@code false}不是暗黑模式
 */
fun Context.isDarkTheme(): Boolean {
    val flag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return flag == Configuration.UI_MODE_NIGHT_YES
}

/**
 * 启动活动
 * @receiver Activity
 * @param clazz Class<Activity> 活动
 */
fun <T: Activity> Activity.startActivity(clazz: Class<T>) {
    startActivity(Intent(this, clazz))
}
