package com.letter.presenter

import android.view.View

/**
 * View Presenter
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
interface ViewPresenter : View.OnClickListener{
    override fun onClick(v: View?)
}