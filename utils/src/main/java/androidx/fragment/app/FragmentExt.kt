package androidx.fragment.app

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Fragment 扩展
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */

/**
 * toast
 * @receiver Fragment
 * @param message String 消息
 * @param duration Int 提示时长
 */
fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT)
        = Toast.makeText(this.requireContext(), message, duration).show()

/**
 * Toast
 * @receiver Fragment
 * @param resId Int 文本资源id
 * @param duration Int 提示时长
 */
fun Fragment.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT)
        = Toast.makeText(this.requireContext(), resources.getText(resId), duration).show()

/**
 * 启动活动
 * @receiver Context
 * @param clazz Class<Activity> 活动
 * @param act  [@kotlin.ExtensionFunctionType] Function1<Intent, Unit>? Intent执行动作
 */
fun <T: Activity> Fragment.startActivity(clazz: Class<T>, act: (Intent.()->Unit)? = null) {
    val intent = Intent(requireContext(), clazz)
    act?.invoke(intent)
    requireContext().startActivity(intent)
}

/**
 * 启动活动
 * @receiver Context context
 * @param action String 动作
 * @param act [@kotlin.ExtensionFunctionType] Function1<Intent, Unit>? Intent执行动作
 */
fun Fragment.startActivity(action: String, act: (Intent.()->Unit)? = null) {
    val intent = Intent(action)
    act?.invoke(intent)
    requireContext().startActivity(intent)
}