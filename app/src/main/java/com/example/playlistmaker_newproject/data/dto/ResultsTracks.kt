package com.example.playlistmaker_newproject.data.dto

import com.example.playlistmaker_newproject.domain.models.Track
import okhttp3.Response

class ResultsTracks(
    val results: List<TrackDto>
): TrackResponse() {
}