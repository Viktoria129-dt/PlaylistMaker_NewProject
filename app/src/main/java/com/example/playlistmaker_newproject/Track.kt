package com.example.playlistmaker_newproject

import android.R.attr.name
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class Track(
    val trackName:String,
    val artistName:String,
    val trackTimeMillis: String,
    val artworkUrl100:String
)

