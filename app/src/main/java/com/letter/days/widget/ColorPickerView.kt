package com.letter.days.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.LinearLayout
import com.letter.days.R

/**
 * @author Letter(zhangkeqiang@ut.cn)
 * @version 1.0
 */
class ColorPickerView @JvmOverloads
constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int, defStyleRes: Int):
        LinearLayout(context, attrs, defStyleAttr, defStyleRes),
        AdapterView.OnItemClickListener {

    private var gridView: GridView? = null

    private var colorPaneSize: Float
    private var colorPaneStroke: Int

    var selectedColor = 0
    var selectedItem = 0
        set(value) {
            field = value
            (gridView?.adapter?.getItem(value) as ColorPane).checked = true
        }
    private var viewWidth = 0f

    var onColorClickListener: OnColorClickListener? = null

    constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int):
            this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?=null):
            this(context, attrs, 0, 0)

    constructor(context: Context):
            this(context, null, 0, 0)

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_color_pick_view, this)

        gridView = findViewById(R.id.grid_view)

        val attrArray = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerView)

        val colorResourceId = attrArray.getResourceId(R.styleable.ColorPickerView_colorResource, 0)

        colorPaneSize = attrArray.getDimension(R.styleable.ColorPickerView_colorPaneSize, 0f)
        val horizontalSpacing = attrArray.getDimension(R.styleable.ColorPickerView_horizontalSpacing, 0f)
        val verticalSpacing = attrArray.getDimension(R.styleable.ColorPickerView_verticalSpacing, 0f)
        val numColumns = attrArray.getInt(R.styleable.ColorPickerView_numColumns, 0)
        colorPaneStroke = attrArray.getColor(R.styleable.ColorPickerView_colorPaneStroke, 0)

        attrArray.recycle()

        viewWidth = colorPaneSize * numColumns + horizontalSpacing * (numColumns - 1)

        if (colorResourceId != 0) {
            val colorStrings = context.resources.getStringArray(colorResourceId)
            setColors(colorStrings)
        }
        gridView?.onItemClickListener = this
        gridView?.columnWidth = colorPaneSize.toInt()
        gridView?.horizontalSpacing = horizontalSpacing.toInt()
        gridView?.verticalSpacing = verticalSpacing.toInt()
        if (numColumns != 0) {
            gridView?.numColumns = numColumns
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        (gridView?.adapter?.getItem(selectedItem) as ColorPane).checked = false
        (gridView?.adapter?.getItem(position) as ColorPane).checked = true
        selectedColor = (gridView?.adapter?.getItem(position) as ColorPane).color
        this.onColorClickListener?.onColorClick(this, selectedColor)
        selectedItem = position
    }

    fun setColors(colorStrings: Array<String>) {
        val colors = arrayListOf<Int>()
        for (colorString in colorStrings) {
            colors.add(Color.parseColor(colorString))
        }
        gridView?.adapter = ColorPickerListAdapter(context, colors, colorPaneSize.toInt(), colorPaneStroke)
    }

    fun setColors(colors: ArrayList<Int>) {
        gridView?.adapter = ColorPickerListAdapter(context, colors, colorPaneSize.toInt(), colorPaneStroke)
    }

    fun cancelSelect() {
        (gridView?.adapter?.getItem(selectedItem) as ColorPane).checked = false
    }

    fun setWidthWrapContent() {
        layoutParams = gridView?.layoutParams
        layoutParams.width = viewWidth.toInt()
        gridView?.layoutParams = layoutParams
    }

    fun getAdapter(): ColorPickerListAdapter? {
        return gridView?.adapter as ColorPickerListAdapter
    }

    interface OnColorClickListener {
        fun onColorClick(view: View, color: Int)
    }
}