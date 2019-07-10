package com.letter.days.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * @author Letter(zhangkeqiang@ut.cn)
 * @version 1.0
 */
class AdjustTextView : TextView {

    constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int, defStyleRes: Int):
            super(context, attrs, defStyleAttr, defStyleRes)

    constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int):
            super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet?=null):
            super(context, attrs)

    constructor(context: Context):
            super(context)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, height)
    }
}