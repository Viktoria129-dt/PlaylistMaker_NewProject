package com.example.playlistmaker_newproject.domain.api

import com.example.playlistmaker_newproject.domain.models.Track

interface SearchHistoryRepository {
    fun getHistory(): List<Track>
    fun addTrack(track: Track)
    fun clearHistory()
}