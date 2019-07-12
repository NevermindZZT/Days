package com.letter.days.anniversary

import android.support.v4.view.ViewPager
import android.view.View
import kotlin.math.abs

/**
 * @author Letter(zhangkeqiang@ut.cn)
 * @version 1.0
 */
class AnniPagerTransformer: ViewPager.PageTransformer {

    private var viewPager: ViewPager? = null

    override fun transformPage(view: View, position: Float) {

        if (viewPager == null) {
            viewPager = view.parent as ViewPager
        }

        val leftInScreen = view.left - viewPager?.scrollX!!
        val centerXInViewPager = leftInScreen + view.measuredWidth / 2
        val offsetX = centerXInViewPager - viewPager?.measuredWidth!! / 2
        val offsetRate = offsetX * 0.38f / viewPager?.measuredWidth!!
        val scaleFactor = 1 - abs(offsetRate)

        if (scaleFactor > 0) {
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
        }

    }
}