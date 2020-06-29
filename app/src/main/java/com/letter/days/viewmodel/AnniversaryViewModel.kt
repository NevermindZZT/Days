package com.letter.days.viewmodel

import android.content.Intent
import android.content.startActivity
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.letter.days.database.AppDatabase
import com.letter.days.database.entity.AnniversaryEntity
import com.letter.days.fragment.AnniversaryFragment
import com.letter.days.utils.getApplication
import com.letter.days.utils.getDistance
import com.letter.days.view.SharedAnniversaryView
import com.letter.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 纪念日ViewModel
 * @property daysList MutableLiveData<List<AnniversaryEntity>> 日期列表
 * @property fragmentList MutableLiveData<List<AnniversaryFragment>> fragment列表
 * @property currentPosition MutableLiveData<Int> 当前位置
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class AnniversaryViewModel : ViewModel() {

    val daysList = MutableLiveData<MutableList<AnniversaryEntity>>()
    val fragmentList: MutableLiveData<List<AnniversaryFragment>> = MutableLiveData()
    val currentPosition: MutableLiveData<Int> = MutableLiveData()

    /**
     * 加载纪念日
     */
    @Synchronized
    fun loadDays() {
        viewModelScope.launch {
            daysList.value = AppDatabase.instance(getApplication()).anniversaryDao().getAll().toMutableList()
            if (PreferenceManager.getDefaultSharedPreferences(getApplication()).getBoolean("time_line", false)) {
                daysList.value?.sortWith(compareBy(
                    {it.getDistance(AnniversaryEntity.DistanceMode.DISTANCE_NEXT)},
                    {it.getDistance(AnniversaryEntity.DistanceMode.DISTANCE_ABS)}))
            }
            val fragments = mutableListOf<AnniversaryFragment>()
            if (daysList.value?.isNotEmpty() == true) {
                for (i in 0 until daysList.value?.size!!) {
                    fragments.add(AnniversaryFragment.newInstance(i))
                }
            }
            if (fragments.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    fragmentList.value = fragments
                }
            }
        }
    }

    /**
     * 删除当前
     */
    fun deleteCurrent() {
        viewModelScope.launch {
            AppDatabase.instance(getApplication()).anniversaryDao()
                .delete(daysList.value?.get(currentPosition.value !!) !!)
        }
    }

    /**
     * 根据id获取位置
     * @param id Int id
     * @return Int 位置
     */
    fun getFragmentPosition(id: Int): Int {
        for (i in 0 until (daysList.value?.size ?: 0)) {
            if (daysList.value?.get(i)?.id == id) {
                return i
            }
        }
        return 0
    }

    /**
     * 获取当前id
     * @return Int id
     */
    fun getCurrentAnniversaryId() = daysList.value?.get(currentPosition.value!!)?.id ?: -1

    /**
     * 分享当前纪念日
     */
    fun shareCurrentAnniversary() {
        viewModelScope.launch {
            val sharedAnniversaryView = SharedAnniversaryView(getApplication(),
                anniversary = daysList.value?.get(currentPosition.value ?: 0))
            val file = FileUtils.saveBitmapToExternalFilesDir(getApplication(),
                Environment.DIRECTORY_PICTURES,
                "tmp.jpg",
                sharedAnniversaryView.getBitmap())
            if (file != null) {
                withContext(Dispatchers.Main) {
                    getApplication().startActivity(Intent.ACTION_SEND) {
                        type = "image/*"
                        putExtra(Intent.EXTRA_STREAM,
                            FileProvider.getUriForFile(getApplication(),
                                "com.letter.days.fileprovider",
                                file))
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                }
            }
        }
    }
}