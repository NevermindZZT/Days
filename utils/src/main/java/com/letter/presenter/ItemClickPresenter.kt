package com.letter.presenter

/**
 * 列表item点击 Presenter
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
interface ItemClickPresenter {
    fun onItemClick(adapter: Any, position: Int)
}