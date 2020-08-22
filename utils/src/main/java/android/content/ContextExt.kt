package android.content

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context.NOTIFICATION_SERVICE
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes

/**
 * Context 扩展
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
 * 启动活动
 * @receiver Context context
 * @param action String 动作
 * @param act [@kotlin.ExtensionFunctionType] Function1<Intent, Unit>? Intent执行动作
 */
fun Context.startActivity(action: String, act: (Intent.()->Unit)? = null) {
    val intent = Intent(action)
    act?.invoke(intent)
    startActivity(intent)
}

/**
 * 启动服务
 * @receiver Context context
 * @param clazz Class<T> 服务
 * @param act [@kotlin.ExtensionFunctionType] Function1<Intent, Unit>? Intent执行动作
 */
fun <T: Service> Context.startService(clazz: Class<T>, act: (Intent.()->Unit)? = null) {
    val intent = Intent(this, clazz)
    act?.invoke(intent)
    startService(intent)
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

/**
 * 创建通知渠道
 * @receiver Context context
 * @param channelId String 渠道ID
 * @param channelName String 渠道名
 * @param importance Int 通知重要性
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Context.createNotificationChannel(channelId: String, channelName: String, importance: Int) {
    val channel = NotificationChannel(channelId, channelName, importance)
    (getSystemService(NOTIFICATION_SERVICE) as NotificationManager?)?.createNotificationChannel(channel)
}

/**
 * 获取真实屏幕宽度
 * @receiver Context context
 * @return Int 宽度
 */
fun Context.getRealScreenWidth(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager?
    val displayMetrics = DisplayMetrics()
    windowManager?.defaultDisplay?.getRealMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

/**
 * 获取正式屏幕高度
 * @receiver Context context
 * @return Int 高度
 */
fun Context.getRealScreenHeight(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager?
    val displayMetrics = DisplayMetrics()
    windowManager?.defaultDisplay?.getRealMetrics(displayMetrics)
    return displayMetrics.heightPixels
}

/**
 * 获取屏幕宽度(应用可用宽度)
 * @receiver Context context
 * @return Int 宽度
 */
fun Context.getScreenWidth(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager?
    val point = Point()
    windowManager?.defaultDisplay?.getSize(point)
    return point.x
}

/**
 * 获取屏幕高度(应用可用高度)
 * @receiver Context context
 * @return Int 高度
 */
fun Context.getScreenHeight(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager?
    val point = Point()
    windowManager?.defaultDisplay?.getSize(point)
    return point.y
}