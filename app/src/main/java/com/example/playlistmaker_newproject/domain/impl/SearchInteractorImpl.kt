package com.example.playlistmaker_newproject.domain.impl

import android.os.Looper
import com.example.playlistmaker_newproject.domain.api.SearchInteractor
import com.example.playlistmaker_newproject.domain.api.TrackRepository
import com.example.playlistmaker_newproject.domain.models.Track

class SearchInteractorImpl(private val searchRepository: TrackRepository) : SearchInteractor {
    override fun searchTracks(query: String, callback: SearchInteractor.SearchCallback): List<Track> {
        Thread {
            try {
                val tracks = searchRepository.searchTracks(query)
                android.os.Handler(Looper.getMainLooper()).post {
                    callback.onSuccess(tracks)
                }
            } catch (e: Exception) {
                android.os.Handler(Looper.getMainLooper()).post {
                    callback.onError(e.message ?: "Unknown error")
                }
            }
        }.start()
        return emptyList()
    }
}