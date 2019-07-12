package com.letter.days.widget

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.letter.days.R

/**
 * @author Letter(zhangkeqiang@ut.cn)
 * @version 1.0
 */
class ColorPickerDialog(context: Context, theme: Int) : Dialog(context, theme),
        View.OnClickListener,
        ColorPickerView.OnColorClickListener {

    private var colorPickerMain: ColorPickerView
    private var colorPickerSub: ColorPickerView
    private var positiveButton: Button
    private var negativeButton: Button

    private var selectColor: Int = 0

    private var colors: ArrayList<Int> = arrayListOf(0, 0, 0 , 0)

    private var onColorSelectListener: OnColorSelectListener? = null

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
        colorPickerMain.onColorClickListener = this

        colorPickerSub.setWidthWrapContent()
        colorPickerSub.onColorClickListener = this
    }

    constructor(context: Context): this(context, R.style.DialogTheme)

    constructor(builder: Builder): this(builder.context, builder.getTheme()) {
        this.onColorSelectListener = builder.getOnColorSelectListener()
        this.selectColor = builder.getColor()
        freshColorPickerSub(selectColor, colors.size - 1)
        colorPickerSub.selectedItem = colors.size - 1
    }

    override fun onClick(v: View?) {
        when (v) {
            positiveButton -> {
                onColorSelectListener?.onColorSelect(selectColor)
                dismiss()
            }
            negativeButton -> {
                dismiss()
            }
        }
    }

    override fun onColorClick(view: View, color: Int) {
        when (view) {
            colorPickerMain -> {
                freshColorPickerSub(color, colors.size)
                selectColor = color
            }
            colorPickerSub -> {
                colorPickerMain.cancelSelect()
                selectColor = color
            }
        }
    }

    private fun freshColorPickerSub(color: Int, level: Int) {
        Log.d("Color", "color:$color")
        val stepR = (0XFF - color.and(0x00FF0000).ushr(16)) / level
        val stepG = (0xFF - color.and(0x0000FF00).ushr(8)) / level
        val stepB = (0xFF - color.and(0x000000FF)) / level
        for (i in colors.indices) {
            colors[i] = (color + (stepR * (level - i)).shl(16)
                    + (stepG * (level - i)).shl(8)
                    + (stepB * (level - i)))
        }
        colorPickerSub.setColors(colors)
    }

    class Builder(var context: Context) {
        private var theme = R.style.DialogTheme
        private var onColorSelectListener: OnColorSelectListener? = null
        private var color = 0

        fun setTheme(theme: Int): Builder {
            this.theme = theme
            return this
        }

        fun getTheme(): Int {
            return theme
        }

        fun getOnColorSelectListener(): OnColorSelectListener? {
            return onColorSelectListener
        }

        fun setOnColorSelectListener(onColorSelectListener: OnColorSelectListener): Builder {
            this.onColorSelectListener = onColorSelectListener
            return this
        }

        fun getColor(): Int {
            return color
        }

        fun setColor(color: Int): Builder {
            this.color = color
            return this
        }

        fun create(): ColorPickerDialog {
            return ColorPickerDialog(this)
        }
    }

    interface OnColorSelectListener {
        fun onColorSelect(color: Int)
    }
}