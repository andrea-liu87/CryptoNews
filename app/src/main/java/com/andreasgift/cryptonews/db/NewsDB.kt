package com.andreasgift.cryptonews.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andreasgift.cryptonews.dao.NewsDao
import com.andreasgift.cryptonews.model.News

@Database(entities = [News::class], version = 1, exportSchema = true)
abstract class NewsDB : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var instance: NewsDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NewsDB::class.java,
                "news.db"
            ).build()
    }
}