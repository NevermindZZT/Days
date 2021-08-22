package com.letter.days.activity

import android.content.isDarkTheme
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity

/**
 * 基础活动
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
open class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        setLightStatusBar(!isDarkTheme())
    }

    protected fun setLightStatusBar(set: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController
                ?.setSystemBarsAppearance(
                    if (set) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (set) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility =
                    window.decorView.systemUiVisibility and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
            }
        }
    }
}
