package com.andreasgift.cryptonews

import android.content.Context
import com.andreasgift.cryptonews.model.News
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface RetrofitAPI {
    @GET("/news")
    suspend fun fetchNews(): Response<List<News>>

    companion object {
        var apiInterface: RetrofitAPI? = null
        var BASE_URL = "https://latest-crypto-news1.p.rapidapi.com"

        fun create(context: Context): RetrofitAPI {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .addInterceptor(Interceptor { chain ->
                    val newRequest: Request = chain.request().newBuilder()
                        .addHeader("x-rapidapi-host", "latest-crypto-news1.p.rapidapi.com")
                        .addHeader("x-rapidapi-key", "914065adc3mshc0a1f0e93c0d37cp159961jsn9f6547b5c5c1")
                        .build()
                    chain.proceed(newRequest)
                })
                .addInterceptor(loggingInterceptor)
                .build()


            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build()

            if (apiInterface == null) {
                apiInterface = retrofit.create(RetrofitAPI::class.java)
            }

            return apiInterface!!
        }
    }

}