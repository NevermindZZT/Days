package com.letter.days.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.letter.days.database.entity.AnniversaryEntity

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
}