package androidx.fragment.app

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