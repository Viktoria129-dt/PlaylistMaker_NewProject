package com.example.playlistmaker_newproject

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(val sharedPreferences: SharedPreferences) {
    companion object{
        const val HISTORY_KEY = "search_history"
        const val MAX_HISTORY_SIZE = 10
    }

    fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(HISTORY_KEY, null)?: return emptyList()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    fun saveHistory(track: List<Track>){
        val json = Gson().toJson(track)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }

    fun addTrack(track:Track){
        val historySearch = getHistory().toMutableList()

        historySearch.removeAll { it.trackId == track.trackId }

        historySearch.add(0, track)

        val limitedHistory = if (historySearch.size > 10) historySearch.take(10) else historySearch

        saveHistory(limitedHistory)
    }

    fun clearHistory(){
        sharedPreferences.edit()
            .remove(HISTORY_KEY)
            .apply()
    }

}


