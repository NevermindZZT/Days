package com.letter.days.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.letter.days.database.entity.AnniversaryEntity

/**
 * Anniversary Dao
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
@Dao
interface AnniversaryDao {

    /**
     * 插入纪念日数据
     * @param anniversaries Array<out AnniversaryEntity> 纪念日数据
     */
    @Insert
    fun insert(vararg anniversaries: AnniversaryEntity)

    /**
     * 获取所有纪念日数据
     * @return List<AnniversaryEntity> 纪念日数据列表
     */
    @Query("SELECT * FROM anniversary")
    fun getAll(): List<AnniversaryEntity>

    /**
     * 获取纪念日数据
     * @param id Int 数据id
     * @return AnniversaryEntity 纪念日数据
     */
    @Query("SELECT * FROM anniversary where id = :id")
    fun get(id: Int): AnniversaryEntity

    /**
     * 更新纪念日数据
     * @param anniversary AnniversaryEntity 金纪念日数据
     */
    @Update
    fun update(anniversary: AnniversaryEntity)
}