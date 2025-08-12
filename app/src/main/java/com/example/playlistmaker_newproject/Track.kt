package com.example.playlistmaker_newproject

import android.R.attr.name
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class Track(
    val trackName:String,
    val artistName:String,
    val trackTimeMillis: Long,
    val artworkUrl100:String
)

fun formatTrackDuration(durationMs: Long): String {
    return try {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        dateFormat.format(Date(durationMs))
    } catch (e: Exception) {
        "00:00"
    }
}

