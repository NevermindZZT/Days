package com.letter.days.adapter

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.ImageUtils
import java.io.FileInputStream

/**
 * DataBinding 适配器
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.1
 */

/**
 * 设置虚化背景
 * @receiver View view
 * @param content String? 图片路径
 */
@BindingAdapter("blurBackground")
fun View.setBlurBackground(content: String?) {
    try {
        if (content?.isNotEmpty() == true) {
            background = BitmapDrawable(
                context.resources,
                ImageUtils.fastBlur(
                    BitmapFactory.decodeStream(FileInputStream(content)),
                    0.3f,
                    25f
                )
            )
        }
    } catch (e: Exception) {

    }
}