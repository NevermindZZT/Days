package com.letter.days.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.startService
import androidx.preference.PreferenceManager

/**
 * 开机广播接收器
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.4
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                if (PreferenceManager.getDefaultSharedPreferences(context)
                        .getBoolean("intent_notification", false)) {
                    context.startService(CoreService::class.java)
                }
            }
        }
    }
}