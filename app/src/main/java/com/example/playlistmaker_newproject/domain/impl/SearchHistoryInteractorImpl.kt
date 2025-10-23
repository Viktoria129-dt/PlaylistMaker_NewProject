package com.example.playlistmaker_newproject.domain.impl

import com.example.playlistmaker_newproject.data.network.SearchHistoryRepositoryImpl
import com.example.playlistmaker_newproject.domain.api.SearchHistoryInteractor
import com.example.playlistmaker_newproject.domain.models.Track

class SearchHistoryInteractorImpl(private val trackRepository: SearchHistoryRepositoryImpl) : SearchHistoryInteractor {

    override fun getHistory(): List<Track> {
        return trackRepository.getHistory()
    }

    override fun addTrack(track: Track) {
        trackRepository.addTrack(track)
    }

    override fun clearHistory() {
        trackRepository.clearHistory()
    }
}

