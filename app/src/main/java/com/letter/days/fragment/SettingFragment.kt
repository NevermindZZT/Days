package com.letter.days.fragment

import android.appwidget.AppWidgetManager
import android.content.sendBroadcast
import android.content.startService
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.*
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.files.fileChooser
import com.letter.days.LetterApplication
import com.letter.days.R
import com.letter.days.service.CoreService
import com.letter.days.viewmodel.SettingViewModel

/**
 * 设置Fragment
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class SettingFragment : PreferenceFragmentCompat() {

    private val model by lazy {
        ViewModelProvider.AndroidViewModelFactory
            .getInstance(LetterApplication.instance()).create(SettingViewModel::class.java)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preferences, rootKey)
        val themeModePreference = findPreference<ListPreference>("theme_mode")
        themeModePreference?.setOnPreferenceChangeListener { _, newValue ->
            AppCompatDelegate.setDefaultNightMode((newValue as String).toInt())
            true
        }
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
            "backup" -> {
                model.backup {
                    MaterialDialog(requireContext()).show {
                        message(R.string.activity_setting_dialog_backup_fail_message)
                        positiveButton(R.string.dialog_positive_button)
                    }
                }
            }
            "restore" -> {
                model.requestPermission {
                    val initialFolder = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

                    MaterialDialog(requireContext()).show {
                        fileChooser(requireContext(), initialDirectory = initialFolder) { dialog, file ->
                            model.restore(file)  {
                                MaterialDialog(requireContext()).show {
                                    message(R.string.activity_setting_dialog_restore_fail_message)
                                    positiveButton(R.string.dialog_positive_button)
                                }
                            }
                            dialog.dismiss()
                        }
                        positiveButton(R.string.dialog_positive_button)
                        negativeButton(R.string.dialog_negative_button)
                    }
                }
            }
        }
        return true
    }
}