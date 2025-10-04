package com.example.playlistmaker_newproject.domain.api

import com.example.playlistmaker_newproject.data.dto.TrackDto
import com.example.playlistmaker_newproject.domain.models.Track

interface TrackRepository {
    fun searchTracks(query: String): List<Track>
}