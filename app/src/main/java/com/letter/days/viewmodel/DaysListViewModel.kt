package com.letter.days.viewmodel

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.letter.days.database.AppDatabase
import com.letter.days.database.entity.AnniversaryEntity
import com.letter.days.database.entity.WidgetEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    /**
     * 加载纪念日数据
     */
    @Synchronized
    fun loadDays() {
        viewModelScope.launch {
            daysList.value?.clear()
            daysList.value?.addAll(AppDatabase.instance(getApplication()).anniversaryDao().getAll())
        }
    }

    /**
     * 保存Widget数据
     * @param widgetId Int widget id
     * @param anniId Int 纪念日id
     */
    fun saveWidgetInfo(widgetId: Int, anniId: Int) {
        viewModelScope.launch {
            AppDatabase.instance(getApplication()).widgetDao().insert(
                WidgetEntity(widgetId, anniId)
            )
            withContext(Dispatchers.Main) {
                val intent = Intent()
                intent.apply {
                    action = "android.appwidget.action.APPWIDGET_UPDATE"
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                }
                getApplication<Application>().sendBroadcast(intent)
            }
        }
    }
}