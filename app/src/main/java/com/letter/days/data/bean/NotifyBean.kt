package com.letter.days.data.bean

data class NotifyBean(var notify: List<NotifyItemBean>? = null) {

    data class NotifyItemBean(val time: String? = null,
                              val title: String? = null,
                              val content: List<String>? = null)

}

val NotifyBean.NotifyItemBean.hour: Int
    get() {
        return time?.split(":")?.get(0)?.toInt() ?: 0
    }

val NotifyBean.NotifyItemBean.minute: Int
    get() {
        return time?.split(":")?.get(1)?.toInt() ?: 0
    }
