package com.example.playlistmaker_newproject.data.network

import android.content.SharedPreferences
import com.example.playlistmaker_newproject.domain.api.SearchHistoryRepository
import com.example.playlistmaker_newproject.domain.models.Track
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences): SearchHistoryRepository {
    companion object {
        const val HISTORY_KEY = "search_history"
        const val MAX_HISTORY_SIZE = 10
    }

    override fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return emptyList()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    override fun addTrack(track: Track) {
        val historySearch = getHistory().toMutableList()
        historySearch.removeAll { it.trackId == track.trackId }
        historySearch.add(0, track)
        val limitedHistory = if (historySearch.size > MAX_HISTORY_SIZE) historySearch.take(MAX_HISTORY_SIZE) else historySearch
        saveHistory(limitedHistory)
    }

    override fun clearHistory() {
        sharedPreferences.edit()
            .remove(HISTORY_KEY)
            .apply()
    }

    private fun saveHistory(track: List<Track>) {
        val json = Gson().toJson(track)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }
}