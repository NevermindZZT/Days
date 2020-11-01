package com.letter.days.data.bean

data class NotifyBean(val notify: List<NotifyItemBean>? = null) {

    data class NotifyItemBean(val time: String? = null,
                              val title: String? = null,
                              val content: List<String>? = null)
}

