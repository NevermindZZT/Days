package com.letter.days.utils

import androidx.lifecycle.MutableLiveData

/**
 * LiveData扩展
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */

/**
 * LiveData 通知数据更新
 * @receiver MutableLiveData<T> LiveData
 */
fun <T> MutableLiveData<T>.notify() {
    this.value = this.value
}