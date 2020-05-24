package com.letter.colorpicker

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

/**
 * 颜色选择器对话框
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class ColorPickerDialog
constructor(context: Context, theme: Int = 0, init: (ColorPickerDialog.() -> Unit) ?= null) :
    Dialog(context, theme),
        View.OnClickListener {

    private var colorPickerMain: ColorPickerView                    /* 颜色选择器主视图 */
    private var colorPickerSub: ColorPickerView                     /* 颜色选择器副视图 */
    private var positiveButton: Button                              /* 确认按钮 */
    private var negativeButton: Button                              /* 取消按钮 */

    private var selectColor: Int = 0                                /* 被选中的颜色 */

    private lateinit var colors: Array<Int>                         /* 副视图颜色 */

    private var onColorSelectListener: ((dialog: Dialog, color: Int)-> Unit)? = null    /* 监听 */

    init {
        val view =  LayoutInflater.from(context).inflate(R.layout.dialog_color_picker, null)
        addContentView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))

        colorPickerMain = view.findViewById(R.id.color_picker_main)
        colorPickerSub = view.findViewById(R.id.color_picker_sub)
        positiveButton = view.findViewById(R.id.positive_button)
        negativeButton = view.findViewById(R.id.negative_button)

        positiveButton.setOnClickListener(this)
        negativeButton.setOnClickListener(this)

        colorPickerMain.setWidthWrapContent()
        colorPickerMain.onColorClickListener = ::onColorClick

        colorPickerSub.setWidthWrapContent()
        colorPickerSub.onColorClickListener = ::onColorClick

        if (init != null) {
            this.init()
        }
    }

    constructor(builder: Builder): this(builder.context, builder.theme) {
        this.onColorSelectListener = builder.getOnColorSelectListener()
        this.selectColor = builder.getSelectedColor()
        if (builder.getColors().isNotEmpty()) {
            setColors(builder.getColors())
        }
        if (builder.getColumns() > 0) {
            colorPickerMain.numColumns = builder.getColumns()
            colorPickerSub.numColumns = builder.getColumns()
            colorPickerMain.setWidthWrapContent()
            colorPickerSub.setWidthWrapContent()
            colors = Array(builder.getColumns(), init = {0})
        } else {
            colors = Array(4, init = {0})
        }
        freshColorPickerSub(selectColor, colors.size)
        colorPickerSub.selectedItem = colors.size - 1
    }

    override fun onClick(v: View?) {
        when (v) {
            positiveButton -> {
                onColorSelectListener?.invoke(this, selectColor)
                dismiss()
            }
            negativeButton -> {
                dismiss()
            }
        }
    }

    /**
     * 颜色盘点击时间处理
     * @param view View
     * @param color 被点击的颜色
     */
    fun onColorClick(view: View, color: Int) {
        when (view) {
            colorPickerMain -> {
                freshColorPickerSub(color, colors.size + 1)
                selectColor = color
            }
            colorPickerSub -> {
                colorPickerMain.cancelSelect()
                selectColor = color
            }
        }
    }

    /**
     * 刷新颜色副视图
     * @param color 颜色
     * @param level 颜色划分等级
     */
    private fun freshColorPickerSub(color: Int, level: Int) {
        Log.d("Color", "color:$color")
        val stepR = (0XFF - color.and(0x00FF0000).ushr(16)) / level
        val stepG = (0xFF - color.and(0x0000FF00).ushr(8)) / level
        val stepB = (0xFF - color.and(0x000000FF)) / level
        for (i in colors.indices) {
            colors[i] = (color + (stepR * (level - i - 1)).shl(16)
                    + (stepG * (level - i - 1)).shl(8)
                    + (stepB * (level - i - 1)))
        }
        if (color.and(0x00FFFFFF) == 0x00FFFFFF) {
            colors[0] = 0
        }
        colorPickerSub.setColors(colors.toList() as ArrayList<Int>)
    }

    /**
     * 设置颜色资源
     * @param colorStrings 颜色资源字符串List
     */
    fun setColors(colorStrings: Array<String>) {
        colorPickerMain.setColors(colorStrings)
    }

    /**
     * 设置颜色资源
     * @param colors 颜色资源List
     */
    fun setColors(colors: ArrayList<Int>) {
        colorPickerMain.setColors(colors)
    }

    class Builder(var context: Context, var theme: Int = 0) {
        private var onColorSelectListener: ((dialog: Dialog, color: Int)-> Unit) ? = null
        private var selectedColor = 0
        private var colors = arrayListOf<Int>()
        private var columns = 0

        /**
         * 获取颜色选择监听
         * @return 监听
         */
        fun getOnColorSelectListener(): ((dialog: Dialog, color: Int)-> Unit)? {
            return onColorSelectListener
        }

        /**
         * 设置颜色选择监听
         * @param onColorSelectListener 监听
         */
        fun setOnColorSelectListener(onColorSelectListener: ((dialog: Dialog, color: Int)-> Unit)): Builder {
            this.onColorSelectListener = onColorSelectListener
            return this
        }

        /**
         * 获取被选择的颜色
         * @return 被选择的颜色
         */
        fun getSelectedColor(): Int {
            return selectedColor
        }

        /**
         * 设置被选择的颜色
         * @param color 被选择的颜色
         */
        fun setSelectedColor(color: Int): Builder {
            this.selectedColor = color
            return this
        }

        /**
         * 获取颜色资源List
         * @return 颜色资源List
         */
        fun getColors(): ArrayList<Int> {
            return colors
        }

        /**
         * 设置颜色资源List
         * @param colorStrings 颜色资源
         */
        fun setColors(colorStrings: Array<String>): Builder {
            for (colorString in colorStrings) {
                colors.add(Color.parseColor(colorString))
            }
            return this
        }

        /**
         * 设置颜色资源List
         * @param colors 颜色资源
         */
        fun setColors(colors: ArrayList<Int>): Builder {
            this.colors = colors
            return this
        }

        /**
         * 获取列数
         * @return 列数
         */
        fun getColumns(): Int {
            return columns
        }

        /**
         * 设置列数
         * @param columns 列数
         */
        fun setColumns(columns: Int): Builder {
            this.columns = columns
            return this
        }

        /**
         * 创建对话框
         * @return 对话框
         */
        fun create(): ColorPickerDialog {
            return ColorPickerDialog(this)
        }
    }

}