package com.letter.days.viewmodel

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.AndroidViewModel
import com.letter.days.data.bean.OpenSourceBean

/**
 * 开源相关View Model
 * @property openSourceList ObservableArrayList<OpenSourceDao> 开源项目列表
 * @constructor 构造一个View Model
 *
 * @author Letter(NevermindZZT@gmail.com)
 * @since 1.0.2
 */
class OpenSourceViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "OpenSourceViewModel"
    }

    val openSourceList = ObservableArrayList<OpenSourceBean>()

    init {
        initData()
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        openSourceList.add(OpenSourceBean("material-dialogs",
            "afollestad",
            "https://github.com/afollestad/material-dialogs",
            "A beautiful, fluid, and extensible dialogs API for Kotlin & Android."))
        openSourceList.add(OpenSourceBean("CalendarView",
            "huanghaibin-dev",
            "https://github.com/huanghaibin-dev/CalendarView",
            "Android上一个优雅、万能自定义UI、支持周视图、自定义周起始、性能高效的日历控件"))
        openSourceList.add(OpenSourceBean("AndroidUtilCode",
            "Blankj",
            "https://github.com/Blankj/AndroidUtilCode",
            "AndroidUtilCode is a powerful & easy to use library for Android."))
        openSourceList.add(OpenSourceBean("gson",
            "google",
            "https://github.com/google/gson",
            "A Java serialization/deserialization library to convert Java Objects into JSON and back"))
    }
}