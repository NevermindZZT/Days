package com.letter.days.transformer

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

/**
 * 纪念日View Pager 切换
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class AnniViewPagerTransformer: ViewPager.PageTransformer {

    override fun transformPage(view: View, position: Float) {

        val viewPager = view.parent as ViewPager

        val leftInScreen = view.left - viewPager.scrollX
        val centerXInViewPager = leftInScreen + view.measuredWidth / 2
        val offsetX = centerXInViewPager - viewPager.measuredWidth / 2
        val offsetRate = offsetX * 0.38f / viewPager.measuredWidth
        val scaleFactor = 1 - abs(offsetRate)

        if (scaleFactor > 0) {
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
        }

    }
}