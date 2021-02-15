package com.letter.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.os.Build

/**
 * App Utils
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
object AppUtils {

    /**
     * 获取Package列表
     * @param context Context context
     * @param type Int see [android.content.pm.PackageManager]
     * @return List<PackageInfo> package列表
     */
    fun getPackageList(context: Context, type: Int) =
        context.packageManager.getInstalledPackages(type)

    /**
     * 获取应用信息里欸包
     * @param context Context context
     * @param type Int see [android.content.pm.PackageManager]
     * @return List<AppInfo> 应用信息列表
     */
    fun getAppInfoList(context: Context, type: Int): List<AppInfo> {
        val appInfoList = mutableListOf<AppInfo>()
        val pm = context.packageManager
        getPackageList(context, type).forEach {
            appInfoList.add(AppInfo(
                it.applicationInfo.loadLabel(pm).toString(),
                it.packageName,
                it.applicationInfo.loadIcon(pm),
                it.versionName,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) it.longVersionCode else it.versionCode.toLong(),
                it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0,
                pm.getLaunchIntentForPackage(it.packageName) != null
            ))
        }
        return appInfoList
    }

    fun getAppInfo(context: Context, packageName: String, type: Int): AppInfo {
        val pm = context.packageManager
        val packageInfo = pm.getPackageInfo(packageName, type)
        return AppInfo(
            packageInfo.applicationInfo.loadLabel(pm).toString(),
            packageInfo.packageName,
            packageInfo.applicationInfo.loadIcon(pm),
            packageInfo.versionName,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) packageInfo.longVersionCode else packageInfo.versionCode.toLong(),
            packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0,
            pm.getLaunchIntentForPackage(packageInfo.packageName) != null
        )
    }
}

/**
 * 应用信息
 * @property name String? 应用名
 * @property packageName String? 包名
 * @property icon Drawable? 图标
 * @property versionName String? 版本名
 * @property versionCode Long 版本号
 * @property isSystem Boolean 是否为系统应用
 * @property hasMainActivity Boolean 是否拥有主活动
 * @constructor 构造器
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
data class AppInfo(
    val name: String? = null,
    val packageName: String? = null,
    val icon: Drawable? = null,
    val versionName: String? = null,
    val versionCode: Long = 0,
    val isSystem: Boolean = false,
    val hasMainActivity:Boolean = false
)