package com.letter.colorpicker

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class ColorPickerListAdapter
        (context: Context?, colors: ArrayList<Int>, colorPaneSize: Int, colorPaneStroke: Int):
        BaseAdapter() {

    private var colorPaneList: MutableList<ColorPane> = mutableListOf()

    init {
        val layoutParams: ViewGroup.LayoutParams =
            if (colorPaneSize == 0) {
                 ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
            } else {
                ViewGroup.LayoutParams(colorPaneSize, colorPaneSize)
            }
        for (color in colors) {
            val colorPane = ColorPane(context!!)
            colorPane.layoutParams = layoutParams
            if (colorPaneSize == 0) {
                colorPane.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
            } else {
                colorPane.layoutParams = ViewGroup.LayoutParams(colorPaneSize, colorPaneSize)
            }
            colorPane.color = color
            colorPane.strokeColor = colorPaneStroke
            colorPane.strokeWidth = colorPaneSize.toFloat() / 32
            colorPaneList.add(colorPane)
        }
    }

    override fun getItem(position: Int): Any {
        return colorPaneList[position]
    }

    override fun getCount(): Int {
        return colorPaneList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return colorPaneList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setItemColor(position: Int, color: Int) {
        colorPaneList[position].color = color
    }

}