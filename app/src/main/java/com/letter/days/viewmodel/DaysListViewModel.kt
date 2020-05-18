package com.letter.days.viewmodel

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.letter.days.database.AppDatabase
import com.letter.days.database.entity.AnniversaryEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * 纪念日列表View Model
 * @property daysList MutableLiveData<ObservableList<AnniversaryEntity>>
 * @constructor 构造一个ViewModel
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class DaysListViewModel(application: Application) : AndroidViewModel(application)  {

    val daysList: MutableLiveData<ObservableList<AnniversaryEntity>> = MutableLiveData(
        ObservableArrayList()
    )

    init {
        loadDays()
    }

    fun loadDays() {
        viewModelScope.launch {
            daysList.value?.clear()
            daysList.value?.addAll(AppDatabase.instance(getApplication()).anniversaryDao().getAll())
        }
    }
}