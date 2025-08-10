package com.example.playlistmaker_newproject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("/search?entity=song")
    fun search(@Query("term") query: String,
               @Query("entity") entity: String = "song",
               @Query("limit") limit: Int = 50): Call<ResultsTracks>
}