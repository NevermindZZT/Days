package com.letter.presenter

import android.widget.Adapter

/**
 * 列表item点击 Presenter
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
interface ItemClickPresenter {
    fun onItemClick(adapter: Adapter, position: Int)
}