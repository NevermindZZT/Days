package com.letter.days.database.entity

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Anniversary Entity
 * @property id Int id
 * @property time Long 时间
 * @property name String? 名字
 * @property type Int 类型
 * @property lunar Boolean 是否为农历
 * @property color Int 主题色
 * @constructor 构造一个纪念日数据
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
@Entity(tableName = "anniversary")
data class AnniversaryEntity
    constructor(@PrimaryKey(autoGenerate = true) var id: Int = 0,
                @ColumnInfo(name = "time") var time: Long = System.currentTimeMillis(),
                @ColumnInfo(name = "name") var name: String? = null,
                @ColumnInfo(name = "type") var type: Int = ANNI_TYPE_ONLY_ONCE,
                @ColumnInfo(name = "lunar") var lunar: Boolean = false,
                @ColumnInfo(name = "color") var color: Int = Color.TRANSPARENT,
                @ColumnInfo(name = "image") var image: String? = null) {

    companion object {
        const val ANNI_TYPE_ONLY_ONCE = 0
        const val ANNI_TYPE_EVERY_YEAR = 1
        const val ANNI_TYPE_COUNT_DOWN = 2

        val ANNI_TYPE_TEXT = arrayOf(
            "纪念日",
            "周年纪念",
            "倒计时"
        )
    }

    enum class DistanceMode {
        DISTANCE_ABS,
        DISTANCE_NEXT,
    }
}