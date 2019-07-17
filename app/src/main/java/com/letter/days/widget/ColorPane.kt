package com.letter.days.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.letter.days.R
import kotlin.math.min

/**
 * @author Letter(zhangkeqiang@ut.cn)
 * @version 1.0
 */
class ColorPane @JvmOverloads
        constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int, defStyleRes: Int):
        View(context, attrs, defStyleAttr, defStyleRes) {

    var color = 0
        set(value) {
            field = value
            invalidate()
        }
    var checked = false
        set(value) {
            field = value
            invalidate()
        }

    var strokeColor = 0
    var strokeWidth = 0f

    private val paint = Paint()

    constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int):
            this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?=null):
            this(context, attrs, 0, 0)

    constructor(context: Context):
            this(context, null, 0, 0)

    init {
        val attrArray = context.obtainStyledAttributes(attrs, R.styleable.ColorPane)

        color = attrArray.getColor(R.styleable.ColorPane_color, 0)
        strokeColor = attrArray.getColor(R.styleable.ColorPane_strokeColor, 0)
        strokeWidth = attrArray.getDimension(R.styleable.ColorPane_strokeWidth, 0f)

        attrArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.color = color
        paint.style = Paint.Style.FILL

        canvas?.drawCircle(width.toFloat() / 2, height.toFloat() / 2,
                (min(width, height) / 2 - if (strokeWidth > 0) 1 else 0).toFloat(), paint)

        if (strokeWidth > 0) {
            paint.color = strokeColor
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = strokeWidth
            canvas?.drawCircle(width.toFloat() / 2, height.toFloat() / 2,
                    (min(width, height) / 2).toFloat() - (strokeWidth) / 2, paint)
        }

        if (checked) {
            val bright = (color.and(0x00FF0000).ushr(16) * 0.3
                    + color.and(0x0000FF00).ushr(8) * 0.6
                    + color.and(0x000000FF) * 0.1)
            if (bright > 0x80 || color.toLong().and(0xFF000000).ushr(24) < 0x20) {
                paint.color = Color.BLACK
            } else {
                paint.color = Color.WHITE
            }
            val lineWidth = height.toFloat() / 20
            val offsetX = -width.toFloat() / 32
            val offsetY = height.toFloat() / 8
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = lineWidth
            canvas?.drawLine(width.toFloat() / 3 + offsetX, height.toFloat() / 3 + offsetY,
                    width.toFloat() / 2 + lineWidth / 2.828f + offsetX,
                    width.toFloat() / 2 + lineWidth / 2.828f + offsetY, paint)
            canvas?.drawLine(width.toFloat() / 2 + offsetX, height.toFloat() / 2 + offsetY,
                    width.toFloat() / 4 * 3 + offsetX, width.toFloat() / 4 + offsetY, paint)
        }
    }

}