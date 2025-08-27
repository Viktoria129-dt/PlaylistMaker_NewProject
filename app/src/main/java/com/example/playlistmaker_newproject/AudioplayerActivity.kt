package com.example.playlistmaker_newproject

import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class AudioplayerActivity : androidx.appcompat.app.AppCompatActivity() {
    lateinit var currentTrack: Track
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audioplayer)
        val buttonBackAudio = findViewById<ImageButton>(R.id.backAudio)
        buttonBackAudio.setOnClickListener{
            finish()
        }

        val track = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("TRACK", Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Track>("TRACK")
        }
        if (track!=null){
             currentTrack = track
             setupPlayerUI(track)
        }
    }
    private fun setupPlayerUI(track: Track){
        val albumImage = findViewById<ImageView>(R.id.beatlesImage)
        val trackName = findViewById<TextView>(R.id.NameOfSong)
        val AuthorName = findViewById<TextView>(R.id.nameOfAuthor)
        val timeOfTrack = findViewById<TextView>(R.id.timeNumbers)
        val albumName = findViewById<TextView>(R.id.nameOfAlbom)
        val yearRelease = findViewById<TextView>(R.id.yearNumbers)
        val genreOfSong = findViewById<TextView>(R.id.nameOfGenre)
        val country = findViewById<TextView>(R.id.nameOfCountry)


        trackName.text = track.trackName
        AuthorName.text = track.artistName
        timeOfTrack.text = formatTrackDuration(track.trackTimeMillis)
        albumName.text = track.collectionName
        yearRelease.text = formatYear(track.releaseDate)
        genreOfSong.text = track.primaryGenreName
        country.text = track.country


        val highQualityCoverUrl = track.getCoverArtwork()
        Glide.with(this)
            .load(highQualityCoverUrl)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(8)))
            .placeholder(R.drawable.placeholder_audio)
            .into(albumImage)
    }
    private fun formatYear(releaseDate: String): String {
        return try {
            releaseDate.substring(0, 4)
        } catch (e: Exception) {
            releaseDate
        }
    }

}

