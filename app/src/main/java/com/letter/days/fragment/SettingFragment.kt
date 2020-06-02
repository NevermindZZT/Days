package com.letter.days.fragment

import android.appwidget.AppWidgetManager
import android.content.sendBroadcast
import android.content.startService
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.letter.days.R
import com.letter.days.service.CoreService

/**
 * 设置Fragment
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            "widget_background" -> {
                context?.sendBroadcast("android.appwidget.action.APPWIDGET_UPDATE") {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
                }
            }
            "intent_notification" -> {
                context?.startService(CoreService::class.java)
            }
        }
        return true
    }
}