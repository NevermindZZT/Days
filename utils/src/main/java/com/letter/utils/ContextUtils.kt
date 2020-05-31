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
 * dp转px
 * @receiver Context context
 * @param dp Int dp
 * @return Float px
 */
fun Context.dp2px(dp: Int): Float  = dp * resources.displayMetrics.density

/**
 * px转dp
 * @receiver Context context
 * @param px Float px
 * @return Float dp
 */
fun Context.px2dp(px: Float): Float = px / resources.displayMetrics.density

/**
 * 启动活动
 * @receiver Context
 * @param clazz Class<Activity> 活动
 * @param act  [@kotlin.ExtensionFunctionType] Function1<Intent, Unit>? Intent执行动作
 */
fun <T: Activity> Context.startActivity(clazz: Class<T>, act: (Intent.()->Unit)? = null) {
    val intent = Intent(this, clazz)
    act?.invoke(intent)
    startActivity(intent)
}

/**
 * 发送广播
 * @receiver Context context
 * @param action String 广播动作
 * @param act [@kotlin.ExtensionFunctionType] Function1<Intent, Unit>? Intent执行动作
 */
fun Context.sendBroadcast(action: String, act: (Intent.()->Unit)? = null) {
    val intent = Intent()
    intent.apply {
        this.action = action
        act?.invoke(this)
    }
    sendBroadcast(intent)
}
