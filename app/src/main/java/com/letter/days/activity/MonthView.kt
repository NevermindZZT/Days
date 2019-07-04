package com.letter.days.activity

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.view.View
import com.haibin.calendarview.Calendar
import kotlin.math.min

/**
 * @author Letter(zhangkeqiang@ut.cn)
 * @version 1.0
 */

class MonthView(context: Context) : com.haibin.calendarview.MonthView(context) {

    private var mRadius: Int = 0

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint)
        mSelectedPaint.maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.SOLID)
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemePaint)
        mSchemePaint.maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.SOLID)
    }

    override fun onPreviewHook() {
        mRadius = min(mItemHeight, mItemWidth) / 5 * 2
    }

    override fun onDrawSelected(canvas: Canvas?, calendar: Calendar?, x: Int, y: Int, hasScheme: Boolean): Boolean {
        val cx: Int = x + mItemWidth / 2
        val cy: Int = y + mItemHeight / 2
        canvas?.drawCircle(cx.toFloat(), cy.toFloat(), mRadius.toFloat(), mSelectedPaint)
        return true
    }

    override fun onDrawScheme(canvas: Canvas?, calendar: Calendar?, x: Int, y: Int) {
        val cx: Int = x + mItemWidth / 2
        val cy: Int = y + mItemHeight / 2
        canvas?.drawCircle(cx.toFloat(), cy.toFloat(), mRadius.toFloat(), mSchemePaint)
    }

    override fun onDrawText(canvas: Canvas?, calendar: Calendar?, x: Int, y: Int, hasScheme: Boolean, isSelected: Boolean) {
        val cx: Float = (x + mItemWidth / 2).toFloat()
        val top: Float = (y - mItemHeight / 8).toFloat()
        when {
            isSelected -> {
                canvas?.drawText(calendar?.day.toString(), cx, mTextBaseLine + top,
                        if (calendar?.isCurrentDay!!) mCurDayTextPaint else mSelectTextPaint)
                canvas?.drawText(calendar?.lunar ?: "", cx, (mTextBaseLine + y + mItemHeight / 10),
                        if (calendar?.isCurrentDay!!) mCurDayLunarTextPaint else mSelectedLunarTextPaint)
            }
            hasScheme -> {
                canvas?.drawText(calendar?.day.toString(), cx, mTextBaseLine + top,
                        if (calendar?.isCurrentDay!!) mCurDayTextPaint else {
                            if (calendar.isCurrentMonth) mSelectTextPaint else mOtherMonthTextPaint
                        })
                canvas?.drawText(calendar?.lunar ?: "", cx, (mTextBaseLine + y + mItemHeight / 10),
                        mSelectedLunarTextPaint)
            }
            else -> {
                canvas?.drawText(calendar?.day.toString(), cx, mTextBaseLine + top,
                        if (calendar?.isCurrentDay!!) mCurDayTextPaint else {
                            if (calendar.isCurrentMonth) mCurMonthTextPaint else mOtherMonthTextPaint
                        })
                canvas?.drawText(calendar?.lunar ?: "", cx, (mTextBaseLine + y + mItemHeight / 10),
                        if (calendar?.isCurrentDay!!) mCurDayLunarTextPaint else mCurMonthLunarTextPaint)
            }
        }
    }
}