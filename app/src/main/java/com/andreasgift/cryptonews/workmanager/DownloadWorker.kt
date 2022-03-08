package com.andreasgift.cryptonews.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.andreasgift.cryptonews.repository.NewsRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@EntryPoint
@InstallIn(SingletonComponent::class)
@Named("news_repo")
interface RepoEntryPoint {
    val newsRepository: NewsRepository
}

@Singleton
class DownloadWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    @Inject var repo: NewsRepository

    init {
        val repoFactory = EntryPointAccessors.fromApplication(
            appContext,
            RepoEntryPoint::class.java
        )
        repo = repoFactory.newsRepository
    }

    override fun doWork(): Result {
        return try {
            fetchNews(repo)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun fetchNews(repository: NewsRepository) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.fetchNewsFromNet()
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    val newsList = response.body()
                    if (newsList != null) {
                        repo.insertNewsToDB(newsList)
                    }
                }
            }
        }
    }
}