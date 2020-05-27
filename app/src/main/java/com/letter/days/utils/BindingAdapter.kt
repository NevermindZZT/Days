package com.letter.days.utils

import android.animation.ObjectAnimator
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

/**
 * 视图绑定适配
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */

/**
 * 设置Progress bar 进度
 * @param progressBar ProgressBar Progress bar
 * @param progress Int 进度
 */
@BindingAdapter("progress")
fun setProgress(progressBar: ProgressBar, progress: Int) {
    val initValue = progressBar.progress
    ObjectAnimator.ofInt(progressBar, "progress", initValue, progress)
        .setDuration(500)
        .start()
}
