package com.example.playlistmaker_newproject.data.network

import com.example.playlistmaker_newproject.data.NetworkClient
import com.example.playlistmaker_newproject.data.dto.ResultsTracks
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
    val apiClient = retrofit.create(APIService::class.java)

    override fun doRequest(dto: Any): ResultsTracks {
        if (dto is String) {
            val resp = apiClient.search(dto).execute()
            val body = resp.body() ?: ResultsTracks(emptyList())
            return body
        } else {
            return ResultsTracks(emptyList())
        }
    }
}