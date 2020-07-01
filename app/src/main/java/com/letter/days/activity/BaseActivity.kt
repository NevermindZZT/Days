package com.letter.days.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.isDarkTheme
import android.view.WindowInsets

/**
 * 基础活动
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isDarkTheme()) {
            /* Android O以上支持，设置浅色状态栏 */
            setLightStatusBar(true)
        }
    }

    protected fun setLightStatusBar(set: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (set) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility =
                    window.decorView.systemUiVisibility and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
            }
        }
    }
}
