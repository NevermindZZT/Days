package com.letter.days.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Widget数据Entity
 * @property id Int widget id
 * @property anniversaryId Int 纪念日id
 * @constructor 构造器
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
@Entity(tableName = "widget")
data class WidgetEntity(@PrimaryKey var id: Int = 0,
                        @ColumnInfo(name = "anniId") var anniversaryId: Int = -1)