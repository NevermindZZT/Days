package android.app

import android.content.Intent

/**
 * Activity 扩展
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */

/**
 * 启动活动并请求结果
 * @receiver Context context
 * @param clazz Class<Activity> 活动
 * @param requestCode Int 请求码
 * @param act [@kotlin.ExtensionFunctionType] Function1<Intent, Unit>? Intent执行动作
 */
fun <T: Activity> Activity.startActivityFroResult(clazz: Class<T>, requestCode: Int, act: (Intent.()->Unit)? = null) {
    val intent = Intent(this, clazz)
    act?.invoke(intent)
    startActivityForResult(intent, requestCode)
}

/**
 * 启动活动
 * @receiver Context context
 * @param action String 动作
 * @param requestCode Int 请求码
 * @param act [@kotlin.ExtensionFunctionType] Function1<Intent, Unit>? Intent执行动作
 */
fun Activity.startActivityForResult(action: String, requestCode: Int, act: (Intent.()->Unit)? = null) {
    val intent = Intent(action)
    act?.invoke(intent)
    startActivityForResult(intent, requestCode)
}