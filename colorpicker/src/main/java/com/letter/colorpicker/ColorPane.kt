package com.letter.colorpicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

/**
 * 颜色盘View
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
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
    set(value) {
        field = value
        invalidate()
    }

    var strokeWidth = 0f
    set(value) {
        field = value
        invalidate()
    }

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
        checked = attrArray.getBoolean(R.styleable.ColorPane_checked, false)

        attrArray.recycle()
    }

    /**
     * 绘制View
     * @param canvas 画布
     */
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
            val diameter = min(width, height)
            val lineWidth = diameter.toFloat() / 20
            val offsetX = -diameter.toFloat() / 32 + (if (width > height) (width - height) / 2 else 0)
            val offsetY = diameter.toFloat() / 8 + (if (height > width) (height - width) / 2 else 0)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = lineWidth
            canvas?.drawLine(diameter.toFloat() / 3 + offsetX, diameter.toFloat() / 3 + offsetY,
                diameter.toFloat() / 2 + lineWidth / 2.828f + offsetX,
                diameter.toFloat() / 2 + lineWidth / 2.828f + offsetY, paint)
            canvas?.drawLine(diameter.toFloat() / 2 + offsetX, diameter.toFloat() / 2 + offsetY,
                diameter.toFloat() / 4 * 3 + offsetX, diameter.toFloat() / 4 + offsetY, paint)
        }
    }

    /**
     * 测量控件大小
     * @param widthMeasureSpec widthMeasureSpec
     * @param heightMeasureSpec heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec))
    }

    /**
     * 测量大小
     * @param measureSpec measureSpec
     * @return 测量结果
     */
    private fun measureSize(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return (if (specMode == MeasureSpec.EXACTLY) specSize
        else (context.resources.displayMetrics.density * 50).toInt())
    }
}