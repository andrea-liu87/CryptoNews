package com.andreasgift.cryptonews.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.andreasgift.cryptonews.model.News
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("DELETE FROM news_table")
    fun deleteAll()

    @Insert
    suspend fun insertAll(newsList: List<News?>?)

    @Query("SELECT * from news_table ORDER BY id ASC")
    fun getAllNews(): LiveData<List<News>>
}