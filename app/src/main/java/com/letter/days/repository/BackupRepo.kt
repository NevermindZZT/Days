package com.letter.days.repository

import android.Manifest
import android.content.Context
import android.content.toast
import android.os.Build
import android.os.Environment
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.PermissionUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.letter.days.R
import com.letter.days.database.AppDatabase
import com.letter.days.database.entity.AnniversaryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

private const val TAG = "BackupRepo"

/**
 * 备份恢复Repository
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class BackupRepo {

    fun checkPermission(onDenied: (()->Unit)? = null, onGranted: (()->Unit)? = null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionUtils.permission(PermissionConstants.STORAGE)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        onGranted?.invoke()
                    }

                    override fun onDenied() {
                        onDenied?.invoke()
                    }
                })
                .request()
        } else {
            onGranted?.invoke()
        }
    }

    suspend fun backup(context: Context, onFail: (()->Unit)? = null) {
        checkPermission(onFail) {
            MainScope().launch(Dispatchers.IO) {
                val anniversaries = AppDatabase.instance(context).anniversaryDao().getAll()
                if (anniversaries.isNotEmpty()) {
                    val data = Gson().toJson(anniversaries)
                    val calendar = Calendar.getInstance(TimeZone.getDefault())
                    val file = File(
                        context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                        "days_${calendar.get(Calendar.YEAR)}_${calendar.get(Calendar.MONTH) + 1}" +
                                "_${calendar.get(Calendar.DAY_OF_MONTH)}_${calendar.get(Calendar.HOUR_OF_DAY)}" +
                                "_${calendar.get(Calendar.MINUTE)}.json")
                    FileIOUtils.writeFileFromString(file, data)
                    withContext(Dispatchers.Main) {
                        context.toast(context.getString(R.string.activity_setting_toast_backup_success).format(file.path))
                    }
                }
            }
        }
    }

    suspend fun restore(context: Context, file: String,  onFail: (()->Unit)? = null) {
        val data = FileIOUtils.readFile2String(file)
        val type = object : TypeToken<List<AnniversaryEntity>>() {}.type
        try {
            val anniversaries = Gson().fromJson<List<AnniversaryEntity>>(data, type)
            if (anniversaries?.isNotEmpty() == true) {
                anniversaries.forEach {
                    it.id = 0
                }
                AppDatabase.instance(context).anniversaryDao().insert(anniversaries)
                withContext(Dispatchers.Main) {
                    context.toast(R.string.activity_setting_toast_restore_success)
                }
            } else {
                onFail?.invoke()
            }
        } catch (e: Exception) {
            onFail?.invoke()
        }
    }
}