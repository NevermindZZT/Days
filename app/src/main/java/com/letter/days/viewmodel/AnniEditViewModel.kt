package com.letter.days.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.letter.days.R
import com.letter.days.database.AppDatabase
import com.letter.days.database.entity.AnniversaryEntity
import com.letter.utils.toast
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
    fun saveAnniversary() {
        viewModelScope.launch {
            val anniversary = anniversary.value
            if (anniversary != null) {
                AppDatabase.instance(getApplication())
                    .anniversaryDao().insert(anniversary)
            }
        }
    }
}