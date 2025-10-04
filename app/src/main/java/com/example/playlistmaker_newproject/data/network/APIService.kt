package com.example.playlistmaker_newproject.data.network

import com.example.playlistmaker_newproject.data.dto.ResultsTracks
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("/search?entity=song")
    fun search(@Query("term") query: String): Call<ResultsTracks>
}