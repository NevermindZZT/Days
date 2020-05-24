package com.letter.colorpicker

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import java.lang.Exception

/**
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class ColorPickerView @JvmOverloads
constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int, defStyleRes: Int):
    GridView(context, attrs, defStyleAttr, defStyleRes),
    AdapterView.OnItemClickListener {

    private var colorPaneSize: Float
    private var colorPaneStroke: Int

    var selectedColor = 0
    var selectedItem = 0
        set(value) {
            field = value
            (this.adapter?.getItem(value) as ColorPane).checked = true
        }

    var onColorClickListener: ((view: View, color: Int) -> Unit)? = null

    constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int):
            this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?=null):
            this(context, attrs, 0, 0)

    constructor(context: Context):
            this(context, null, 0, 0)

    init {
        val attrArray = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerView)

        val colorResourceId = attrArray.getResourceId(R.styleable.ColorPickerView_colorResource, 0)

        colorPaneSize = attrArray.getDimension(R.styleable.ColorPickerView_colorPaneSize, 0f)
        colorPaneStroke = attrArray.getColor(R.styleable.ColorPickerView_colorPaneStroke, 0)

        attrArray.recycle()

        if (colorResourceId != 0) {
            val colorStrings = context.resources.getStringArray(colorResourceId)
            setColors(colorStrings)
        }
        this.onItemClickListener = this
        this.columnWidth = colorPaneSize.toInt()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        (this.adapter?.getItem(selectedItem) as ColorPane).checked = false
        (this.adapter?.getItem(position) as ColorPane).checked = true
        selectedColor = (this.adapter?.getItem(position) as ColorPane).color
        this.onColorClickListener?.invoke(this, selectedColor)
        selectedItem = position
    }

    /**
     * 设置颜色资源
     * @param colorStrings 添加的颜色字符串List
     */
    fun setColors(colorStrings: Array<String>) {
        val colors = arrayListOf<Int>()
        for (colorString in colorStrings) {
            colors.add(Color.parseColor(colorString))
        }
        this.adapter = ColorPickerListAdapter(context, colors, colorPaneSize.toInt(), colorPaneStroke)
    }

    /**
     * 设置颜色资源
     * @param colors 添加的颜色List
     */
    fun setColors(colors: ArrayList<Int>) {
        this.adapter = ColorPickerListAdapter(context, colors, colorPaneSize.toInt(), colorPaneStroke)
    }

    /**
     * 取消当前被选择的颜色
     */
    fun cancelSelect() {
        (this.adapter?.getItem(selectedItem) as ColorPane).checked = false
    }

    /**
     * 设置View为宽度自适应
     */
    fun setWidthWrapContent() {
        try {
            val cls = this.javaClass.superclass
            var field = cls?.getDeclaredField("mRequestedNumColumns")
            field?.isAccessible = true
            val numColumns = field?.getInt(this) ?: 0

            field = cls?.getDeclaredField("mRequestedHorizontalSpacing")
            field?.isAccessible = true
            val horizontalSpacing = field?.getInt(this) ?: 0

            val viewWidth = colorPaneSize * numColumns + horizontalSpacing * (numColumns - 1)
            layoutParams = this.layoutParams
            layoutParams.width = viewWidth.toInt()
            this.layoutParams = layoutParams
        } catch (exception: Exception) {

        }
    }

}