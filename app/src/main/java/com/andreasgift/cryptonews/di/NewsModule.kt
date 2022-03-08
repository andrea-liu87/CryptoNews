package com.andreasgift.cryptonews.di

import android.content.Context
import androidx.room.Room
import com.andreasgift.cryptonews.RetrofitAPI
import com.andreasgift.cryptonews.dao.NewsDao
import com.andreasgift.cryptonews.db.NewsDB
import com.andreasgift.cryptonews.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsModule {

    @Provides
    fun provideAPIService(@ApplicationContext context: Context): RetrofitAPI =
        RetrofitAPI.create(context)

    @Provides
    @Singleton
    @Named("news_repo")
    fun provideRepository(
        api: RetrofitAPI,
        dao: NewsDao
    ): NewsRepository =
        NewsRepository(api, dao)

    @Provides
    @Singleton
    fun provideDao(appDatabase: NewsDB): NewsDao =
        appDatabase.newsDao()

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext appContext: Context): NewsDB {
        return Room.databaseBuilder(
            appContext,
            NewsDB::class.java,
            "news.db"
        ).build()
    }
}