package com.andreasgift.cryptonews.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreasgift.cryptonews.model.News
import com.andreasgift.cryptonews.repository.NewsRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) :
    ViewModel() {

    val newsList = MutableLiveData<List<News>>()

    var job: Job? = null
    var loading = MutableLiveData<Boolean?>()

    fun fetchDataFromNet() {
        job = CoroutineScope(Dispatchers.IO).launch {
            loading.postValue(true)
            val list = repository.fetchNewsFromNet()
            withContext(Dispatchers.Main){
                if (list.isSuccessful){
                    newsList.postValue(list.body())
                }
                loading.postValue(false)
            }
        }
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }
}