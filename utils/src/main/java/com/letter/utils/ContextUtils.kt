package com.letter.utils

import android.content.Context
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
