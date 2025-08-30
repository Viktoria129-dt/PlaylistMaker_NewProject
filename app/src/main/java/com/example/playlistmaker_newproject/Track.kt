package com.example.playlistmaker_newproject

import android.R.attr.name
import android.os.Parcel
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Parcelize
data class Track(
    val trackId: Int,
    val trackName:String,
    val artistName:String,
    val trackTimeMillis: Long,
    val artworkUrl100:String,
    val collectionName:String,
    val releaseDate:String,
    val primaryGenreName:String,
    val country:String
):Parcelable{
    fun getCoverArtwork(): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }
}

fun formatTrackDuration(durationMs: Long): String {
    return try {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        dateFormat.format(Date(durationMs))
    } catch (e: Exception) {
        "00:00"
    }
}


