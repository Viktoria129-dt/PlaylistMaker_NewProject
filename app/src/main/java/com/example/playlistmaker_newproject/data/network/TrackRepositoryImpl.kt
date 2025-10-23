package com.example.playlistmaker_newproject.data.network

import com.example.playlistmaker_newproject.data.NetworkClient
import com.example.playlistmaker_newproject.data.dto.TrackDto
import com.example.playlistmaker_newproject.domain.api.TrackRepository
import com.example.playlistmaker_newproject.domain.models.Track
import com.google.gson.Gson

class TrackRepositoryImpl(private val networkClient: NetworkClient): TrackRepository {
    override fun searchTracks(query: String): List<Track> {
        val response = networkClient.doRequest(query)
        return response.results.map { it.toTrack() }
    }

}
private fun TrackDto.toTrack(): Track {
    return Track(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        artworkUrl100 = artworkUrl100,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl,
        trackTimeMillis = trackTimeMillis
    )


}