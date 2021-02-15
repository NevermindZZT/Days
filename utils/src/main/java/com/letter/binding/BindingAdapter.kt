package com.letter.binding

import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter

private const val TAG = "BindingAdapter"

@BindingAdapter("android:layout_margin")
fun setLayoutMargin(view: View, layoutMargin: Int) {
    try {
        val method = view.layoutParams.javaClass.getMethod("setMargins",
            Int::class.java, Int::class.java, Int::class.java, Int::class.java)
        method.invoke(view.layoutParams, layoutMargin, layoutMargin, layoutMargin, layoutMargin)
        view.layoutParams = view.layoutParams
    } catch (e: Exception) {
        Log.w(TAG, "", e)
    }
}

@BindingAdapter("android:layout_marginHorizontal")
fun setLayoutMarginHorizontal(view: View, layoutMarginHorizontal: Int) {
    try {
        val method = view.layoutParams.javaClass.getMethod("setMargins",
            Int::class.java, Int::class.java, Int::class.java, Int::class.java)
        val topMarginField = view.layoutParams.javaClass.superclass.getDeclaredField("topMargin")
        val bottomMarginField = view.layoutParams.javaClass.superclass.getDeclaredField("bottomMargin")

        method.invoke(view.layoutParams,
            layoutMarginHorizontal,
            topMarginField.getInt(view.layoutParams),
            layoutMarginHorizontal,
            bottomMarginField.getInt(view.layoutParams))

        view.layoutParams = view.layoutParams
    } catch (e: Exception) {
        Log.w(TAG, "", e)
    }
}

@BindingAdapter("android:layout_marginVertical")
fun setLayoutMarginVertical(view: View, layoutMarginVertical: Int) {
    try {
        val method = view.layoutParams.javaClass.getMethod("setMargins",
            Int::class.java, Int::class.java, Int::class.java, Int::class.java)
        val leftMarginField = view.layoutParams.javaClass.superclass.getDeclaredField("leftMargin")
        val rightMarginField = view.layoutParams.javaClass.superclass.getDeclaredField("rightMargin")

        method.invoke(view.layoutParams,
            leftMarginField.getInt(view.layoutParams),
            layoutMarginVertical,
            rightMarginField.getInt(view.layoutParams),
            layoutMarginVertical)

        view.layoutParams = view.layoutParams
    } catch (e: Exception) {
        Log.w(TAG, "", e)
    }
}
