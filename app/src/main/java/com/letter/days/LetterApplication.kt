package com.letter.days

import android.app.Application
import com.letter.days.service.CoreService
import android.content.startService
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

/**
 * Application
 *
 * @author Letter(nevermindzt@gmail.com)
 * @since 1.0.0
 */
class LetterApplication : Application() {

    companion object {
        /**
         * Application 单例
         */
        private var instance: LetterApplication ?= null

        /**
         * 获取Application实例
         * @return ScheduleApplication Application实例
         */
        fun instance() = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startService(CoreService::class.java)

        AppCompatDelegate.setDefaultNightMode(
            PreferenceManager.getDefaultSharedPreferences(this)
                .getString("theme_mode", "-1")?.toInt() ?: -1
        )
    }
}