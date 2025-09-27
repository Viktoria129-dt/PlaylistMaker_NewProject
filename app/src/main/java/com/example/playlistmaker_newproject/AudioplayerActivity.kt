package com.example.playlistmaker_newproject

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import okhttp3.internal.concurrent.formatDuration
import okhttp3.internal.http2.Http2Reader
import java.util.Locale

class AudioplayerActivity : androidx.appcompat.app.AppCompatActivity() {
    lateinit var buttonPlay: ImageButton
    lateinit var timeOfSong: TextView
    private var mediaPlayer = MediaPlayer()
    private var playerState  = STATE_DEFAULT

    private var mainTread: Handler? = null

    private val updateProgressRunnable = object : Runnable {
        override fun run(){
            updateProgress()
            mainTread?.postDelayed(this,300)
        }
    }

    companion object{
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }

    private fun preparePlayer(track: Track){
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            buttonPlay.isEnabled = true
            playerState = STATE_PREPARED
            timeOfSong.text = "00:00"
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            buttonPlay.setImageResource(R.drawable.vector)
            timeOfSong.text = "00:00"
            mainTread?.removeCallbacks(updateProgressRunnable)

        }
    }
    private fun formatTime(milliseconds: Int): String {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
    }
    private fun updateProgress(){
        if (playerState == 2){
            val currentPosition = mediaPlayer.currentPosition
            timeOfSong.text = formatTime(currentPosition)
        }
    }

    private fun pausePlayer(){
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        buttonPlay.setImageResource(R.drawable.vector)
        mainTread?.removeCallbacks(updateProgressRunnable)
    }

    private fun startPlayer(){
        mediaPlayer.start()
        playerState = STATE_PLAYING
        buttonPlay.setImageResource(R.drawable.pause_button)
        mainTread?.post(updateProgressRunnable)
    }

    private fun playbackControl(){
        when(playerState){
            STATE_PLAYING ->{
                pausePlayer()
            }
            STATE_PAUSED ->{
                startPlayer()
            }
            STATE_PREPARED -> {
                startPlayer()
            }
        }
    }
    lateinit var currentTrack: Track
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.audioplayer)
        val buttonBackAudio = findViewById<ImageButton>(R.id.backAudio)
        buttonBackAudio.setOnClickListener {
            finish()
        }
        mainTread = Handler(Looper.getMainLooper())
        val track = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("TRACK", Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Track>("TRACK")
        }
        if (track!=null){
             currentTrack = track
             setupPlayerUI(track)
             preparePlayer(currentTrack)
        }


        buttonPlay = findViewById<ImageButton>(R.id.buttonCenter)
        buttonPlay.setOnClickListener {
            playbackControl()
        }

        timeOfSong = findViewById<TextView>(R.id.timeOfSong)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainTread?.removeCallbacks(updateProgressRunnable)
    }
    private fun setupPlayerUI(track: Track){
        val albumImage = findViewById<ImageView>(R.id.beatlesImage)
        val trackName = findViewById<TextView>(R.id.NameOfSong)
        val AuthorName = findViewById<TextView>(R.id.nameOfAuthor)
        val timeOfTrack = findViewById<TextView>(R.id.timeNumbers)
        val albumName = findViewById<TextView>(R.id.nameOfAlbum)
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

