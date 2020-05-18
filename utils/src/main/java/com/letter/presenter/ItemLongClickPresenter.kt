package com.letter.presenter

/**
 * 列表item长按 Presenter
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
interface ItemLongClickPresenter{
    fun onItemClick(adapter: Any, position: Int): Boolean
}