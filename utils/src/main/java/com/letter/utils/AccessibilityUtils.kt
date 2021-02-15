package com.letter.utils

import android.app.Service
import android.content.Context
import android.provider.Settings
import java.lang.Exception

/**
 * Accessibility utils
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
object AccessibilityUtils {

    /**
     * check if accessibility service has been enabled
     * @param context Context context
     * @param service Class<Service> accessibility service
     * @return Boolean if accessibility service has been enabled
     */
    fun <T : Service> isServiceEnabled(context: Context, service: Class<T>): Boolean {
        try {
            if (Settings.Secure.getInt(context.contentResolver,  Settings.Secure.ACCESSIBILITY_ENABLED, 0) != 1) {
                return false
            }

            val services = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            services?.split(":")?.forEach {
                if (it.equals("${context.packageName}/${service.name}")) {
                    return true
                }
            }
        } catch (e: Exception) {

        }
        return false
    }

}