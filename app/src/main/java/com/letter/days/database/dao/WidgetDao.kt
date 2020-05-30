package com.letter.days.database.dao

import androidx.room.*
import com.letter.days.database.entity.WidgetEntity

@Dao
interface WidgetDao {

    /**
     * 插入widget数据
     * @param widgets Array<out WidgetDao> widget
     */
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insert(vararg widgets: WidgetEntity)

    /**
     * 获取所有widget数据
     * @return List<WidgetEntity> widget数据
     */
    @Query("SELECT * FROM widget")
    suspend fun getAll(): List<WidgetEntity>

    /**
     * 通过ID获取widget数据
     * @param id Int id
     * @return WidgetDao? widget数据
     */
    @Query("SELECT * FROM widget where id = :id")
    suspend fun get(id: Int): WidgetEntity?

    /**
     * 删除widget数据
     * @param widget WidgetDao widget数据
     */
    @Delete
    suspend fun delete(widget: WidgetEntity)

    /**
     * 通过ID删除
     * @param id Int id
     */
    @Query("DELETE FROM widget where id = :id")
    suspend fun deleteById(id: Int)
}