package com.example.playlistmaker_newproject.data

import com.example.playlistmaker_newproject.data.dto.ResultsTracks
import okhttp3.Response

interface NetworkClient {
    fun doRequest(dto:Any): ResultsTracks
}