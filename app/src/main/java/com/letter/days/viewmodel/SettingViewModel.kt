package com.letter.days.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.letter.days.repository.BackupRepo
import kotlinx.coroutines.launch
import java.io.File

class SettingViewModel(application: Application): AndroidViewModel(application) {

    private val backupRepo by lazy {
        BackupRepo()
    }

    fun backup(onFail: (()->Unit)? = null) {
        viewModelScope.launch {
            backupRepo.backup(getApplication()) {
                onFail?.invoke()
            }
        }
    }

    fun restore(file: File, onFail: (()->Unit)? = null) {
        viewModelScope.launch {
            backupRepo.restore(getApplication(), file.path) {
                onFail?.invoke()
            }
        }
    }
}