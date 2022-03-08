package com.andreasgift.cryptonews.repository

import androidx.lifecycle.LiveData
import com.andreasgift.cryptonews.RetrofitAPI
import com.andreasgift.cryptonews.dao.NewsDao
import com.andreasgift.cryptonews.model.News
import javax.inject.Inject

class NewsRepository @Inject constructor(
    val api: RetrofitAPI,
    val dao: NewsDao
) {
    suspend fun fetchNewsFromNet() =
        api.fetchNews()

    suspend fun insertNewsToDB(list: List<News>) =
        dao.insertAll(list)

    fun fetchNewsFromDB(): LiveData<List<News>> =
        dao.getAllNews()

    fun deleteNewsFromDB() =
        dao.deleteAll()
}