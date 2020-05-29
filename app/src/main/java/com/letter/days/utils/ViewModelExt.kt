package com.letter.days.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import com.letter.days.LetterApplication

/**
 * 获取Application
 * @receiver ViewModel view model
 * @return Application Application
 */
fun ViewModel.getApplication(): Application = LetterApplication.instance()