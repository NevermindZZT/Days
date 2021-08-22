package com.letter.days.adapter

import android.graphics.Bitmap
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
            val src = BitmapFactory.decodeStream(FileInputStream(content))
            background = BitmapDrawable(
                context.resources,
                ImageUtils.fastBlur(
                    Bitmap.createBitmap(
                        src,
                        src.width / 8 * 3,
                        src.height / 8 * 3,
                        src.width / 4,
                        src.height / 4
                    ),
                    0.3f,
                    25f
                )
            )
        }
    } catch (e: Exception) {

    }
}