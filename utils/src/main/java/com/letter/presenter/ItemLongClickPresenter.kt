package com.letter.presenter

import android.widget.Adapter

/**
 * 列表item长按 Presenter
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
interface ItemLongClickPresenter {

    fun onItemClick(adapter: Adapter, position: Int): Boolean
}