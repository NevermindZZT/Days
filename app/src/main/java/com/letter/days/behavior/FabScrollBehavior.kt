package com.letter.days.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat


/**
 * Float Action Button 滑动行为
 * @param V: View 视图
 * @property visible Boolean 按钮可见
 * @constructor 构造器
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class FabScrollBehavior<V: View>(context: Context, attrs: AttributeSet)
    : CoordinatorLayout.Behavior<V>(context, attrs) {

    private var visible = true

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ) = (axes == ViewCompat.SCROLL_AXIS_VERTICAL)

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
        if (dyConsumed > 0 && visible) {
            visible = false
            onHide(child)
        } else if (dyConsumed < 0) {
            visible = true
            onShow(child)
        }
    }

    private fun onHide(view: View) {
        ViewCompat.animate(view).translationY((view.height * 2).toFloat()).interpolator =
            AccelerateInterpolator(3F)
    }

    private fun onShow(view: View) {
        ViewCompat.animate(view).translationY(0F).interpolator =
            AccelerateInterpolator(3F)
    }
}