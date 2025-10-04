package com.example.playlistmaker_newproject.domain.api

import com.example.playlistmaker_newproject.domain.models.Track

interface SearchInteractor {
    fun searchTracks(query:String,callback: SearchCallback): List<Track>
    interface SearchCallback {
        fun onSuccess(tracks: List<Track>)
        fun onError(error: String)
    }
}