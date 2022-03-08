package com.andreasgift.cryptonews.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.*
import androidx.work.*
import com.andreasgift.cryptonews.di.CryptoNewsApplication
import com.andreasgift.cryptonews.model.News
import com.andreasgift.cryptonews.repository.NewsRepository
import com.andreasgift.cryptonews.workmanager.DownloadWorker
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    repository: NewsRepository
) :
    ViewModel() {

    val newsList = repository.fetchNewsFromDB()

    var job: Job? = null
    var loading = MutableLiveData<Boolean?>()

    private var workManager = WorkManager.getInstance(CryptoNewsApplication())

    private val networkConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private val newsParameterWorkInfoItems: LiveData<List<WorkInfo>> =
        workManager.getWorkInfosByTagLiveData("news_download")


    internal fun fetchDataFromNet (lifecycleOwner: LifecycleOwner, uiUpdate: () -> Unit) {
        val newsDownloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setConstraints(networkConstraints)
            .addTag("news_download")
            .build()

        workManager.enqueue(newsDownloadRequest)

        newsParameterWorkInfoItems.observe(lifecycleOwner, {
            val workInfo = it[0]
            if (!it.isNullOrEmpty() && workInfo.state.isFinished) {
                uiUpdate
            }
        })
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }
}