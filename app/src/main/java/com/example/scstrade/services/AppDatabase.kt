package com.example.scstrade.services

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.scstrade.model.dao.StockDao
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.summary.KSEIndices

@Database(entities = [StockItem::class,KSEIndices::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun marketDao(): StockDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}