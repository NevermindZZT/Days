package com.letter.days.database.dao

import androidx.room.*
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
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insert(vararg anniversaries: AnniversaryEntity)

    /**
     * 获取所有纪念日数据
     * @return List<AnniversaryEntity> 纪念日数据列表
     */
    @Query("SELECT * FROM anniversary")
    suspend fun getAll(): List<AnniversaryEntity>

    /**
     * 获取纪念日数据
     * @param id Int 数据id
     * @return AnniversaryEntity 纪念日数据
     */
    @Query("SELECT * FROM anniversary where id = :id")
    suspend fun get(id: Int): AnniversaryEntity?

    /**
     * 更新纪念日数据
     * @param anniversary AnniversaryEntity 纪念日数据
     */
    @Update(onConflict=OnConflictStrategy.REPLACE)
    suspend fun update(anniversary: AnniversaryEntity)

    /**
     * 删除纪念日数据
     * @param anniversary AnniversaryEntity 纪念日数据
     */
    @Delete
    suspend fun delete(anniversary: AnniversaryEntity)
}