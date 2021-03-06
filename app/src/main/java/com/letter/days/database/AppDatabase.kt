package com.letter.days.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.letter.days.database.dao.AnniversaryDao
import com.letter.days.database.dao.WidgetDao
import com.letter.days.database.entity.AnniversaryEntity
import com.letter.days.database.entity.WidgetEntity

/**
 * 数据库
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
@Database(entities = [AnniversaryEntity::class, WidgetEntity::class], version = 2)
abstract class AppDatabase: RoomDatabase() {

    /**
     * 获取Anniversary Dao
     * @return AnniversaryDao Anniversary Dao
     */
    abstract fun anniversaryDao(): AnniversaryDao

    /**
     * 获取Widget Dao
     * @return WidgetDao widget dao
     */
    abstract fun widgetDao(): WidgetDao

    companion object {

        /**
         * AppDatabase单例
         */
        private var instance: AppDatabase? =null

        private val MIGRATION1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE anniversary ADD COLUMN image TEXT")
            }
        }

        /**
         * 获取数据库实例
         * @param context Context context
         * @return AppDatabase 数据库实例
         */
        @Synchronized
        fun instance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room
                    .databaseBuilder(context.applicationContext, AppDatabase::class.java, "user.db")
                    .addMigrations(MIGRATION1_2)
                    .build()
            }
            return instance!!
        }
    }
}