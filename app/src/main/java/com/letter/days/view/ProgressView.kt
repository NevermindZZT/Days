package com.letter.days.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.letter.days.R
import android.content.dp2px
import kotlin.math.min
import kotlin.properties.Delegates

/**
 * Progress View
 * @property max Int progress 最大值
 * @property progress Int progress 进度值
 * @property text String? 显示文本
 * @property textSize Float 文本大小
 * @property textColor Int 文本颜色
 * @property strokeWidth Float 进度条宽度
 * @property strokeColor Int 进度条颜色
 * @property strokeBackground Int 进度条背景色
 * @property background Int 背景色
 * @property fitWidth Boolean 是否根据宽度适应高度
 * @property diameter Int 直径
 * @property backgroundPaint Paint 背景画笔
 * @property textPaint Paint 文本画笔
 * @property strokeBackgroundPaint Paint 进度条背景画笔
 * @property strokePaint Paint 进度条画笔
 * @constructor 构造器
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class ProgressView @JvmOverloads
constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int=0, defStyleRes: Int=0)
    : View(context, attrs, defStyleAttr, defStyleRes){

    var max = 0
        set(value) {
            field = value
            invalidate()
        }
    var progress = 0
        set(value) {
            field = value
            invalidate()
        }
    var text: String? = null
        set(value) {
            field = value
            invalidate()
        }
    var textSize = 0f
        set(value) {
            field = value
            invalidate()
        }
    var textColor = 0
        set(value) {
            field = value
            invalidate()
        }
    var strokeWidth = 0f
        set(value) {
            field = value
            invalidate()
        }
    var strokeColor = 0
        set(value) {
            field = value
            invalidate()
        }
    private var strokeBackground by Delegates.notNull<Int>()
    private var background by Delegates.notNull<Int>()
    private var fitWidth by Delegates.notNull<Boolean>()

    private var diameter = 0

    private val backgroundPaint = Paint()
    private val textPaint = Paint()
    private val strokeBackgroundPaint = Paint()
    private val strokePaint = Paint()

    init {
        val attrArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView)
        textSize = attrArray.getDimension(R.styleable.ProgressView_android_textSize, context.dp2px(14))
        textColor = attrArray.getColor(R.styleable.ProgressView_android_textColor, Color.BLACK)
        strokeWidth = attrArray.getDimension(R.styleable.ProgressView_strokeWidth, context.dp2px(2))
        strokeColor = attrArray.getColor(R.styleable.ProgressView_strokeColor, Color.TRANSPARENT)
        strokeBackground = attrArray.getColor(R.styleable.ProgressView_strokeBackground, Color.TRANSPARENT)
        background = attrArray.getColor(R.styleable.ProgressView_android_background, Color.TRANSPARENT)
        fitWidth = attrArray.getBoolean(R.styleable.ProgressView_fitWidth, true)

        max = attrArray.getInt(R.styleable.ProgressView_android_max, 100)
        progress = attrArray.getInt(R.styleable.ProgressView_android_progress, 0)
        text = attrArray.getString(R.styleable.ProgressView_android_text)
        attrArray.recycle()
    }

    /**
     * 绘制View
     * @param canvas Canvas 画布
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        diameter = min(width, height)

        /* 绘制背景 */
        backgroundPaint.color = background
        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.isAntiAlias = true
        canvas?.drawCircle(width.toFloat() / 2, height.toFloat() / 2,
            (min(width, height) / 2 - if (strokeWidth > 0) 1 else 0).toFloat(), backgroundPaint)

        /* 绘制边框背景 */
        strokeBackgroundPaint.color = strokeBackground
        strokeBackgroundPaint.style = Paint.Style.STROKE
        strokeBackgroundPaint.isAntiAlias = true
        strokeBackgroundPaint.strokeWidth = strokeWidth
        canvas?.drawCircle(width.toFloat() / 2, height.toFloat() / 2,
            (diameter / 2).toFloat() - (strokeWidth) / 2, strokeBackgroundPaint)

        /* 绘制边框 */
        strokePaint.color = strokeColor
        strokePaint.style = Paint.Style.STROKE
        strokePaint.isAntiAlias = true
        strokePaint.strokeWidth = strokeWidth
        canvas?.drawArc(((width - diameter) / 2).toFloat() + (strokeWidth) / 2,
            ((height - diameter) / 2).toFloat() + (strokeWidth) / 2,
            (width - (width - diameter) / 2).toFloat() - (strokeWidth) / 2,
            (height - (height - diameter) / 2).toFloat() - (strokeWidth) / 2,
            -90f,
            (progress.toFloat() * 360 / max.toFloat()),
            false,
            strokePaint)

        /* 绘制文本 */
        if (text != null) {
            textPaint.color = textColor
            textPaint.style = Paint.Style.FILL
            textPaint.isAntiAlias = true
            textPaint.textSize = textSize
            textPaint.textAlign = Paint.Align.CENTER
            val fontMetricsInt = textPaint.fontMetricsInt
            canvas?.drawText(text!!,
                (width / 2).toFloat(),
                (height / 2 - fontMetricsInt.descent + (fontMetricsInt.bottom - fontMetricsInt.top) / 2).toFloat(),
                textPaint)
        }
    }

    /**
     * 测量控件大小
     * @param widthMeasureSpec widthMeasureSpec
     * @param heightMeasureSpec heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        setMeasuredDimension(measureSize(widthMeasureSpec),
            if (fitWidth) height else measureSize(heightMeasureSpec))
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