package com.letter.days.viewmodel

import android.app.Application
import android.net.Uri
import android.net.toBitmap
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ImageUtils
import com.letter.days.database.AppDatabase
import com.letter.days.database.entity.AnniversaryEntity
import com.letter.days.utils.getApplication
import com.letter.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 纪念日编辑ViewModel
 * @property anniversary MutableLiveData<AnniversaryEntity>
 * @constructor 构造一个ViewModel
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class AnniEditViewModel(application: Application) : AndroidViewModel(application) {

    val anniversary: MutableLiveData<AnniversaryEntity> = MutableLiveData()

    /**
     * 保存纪念日
     */
    fun saveAnniversary(action: (() -> Unit)? = null) {
        viewModelScope.launch {
            val anniversary = anniversary.value
            if (anniversary != null) {
                AppDatabase.instance(getApplication())
                    .anniversaryDao().insert(anniversary)
            }
            withContext(Dispatchers.Main) {
                action?.invoke()
            }
        }
    }

    fun saveUri2File(uri: Uri?): String? {
        val path = uri?.path ?: return null
        val file = FileUtils.saveBitmapToExternalFilesDir(getApplication(),
            Environment.DIRECTORY_PICTURES,
            "${path.substring(path.lastIndexOf("/") + 1, path.length)}.jpg",
            uri.toBitmap(getApplication()))
        return file?.path
    }
}