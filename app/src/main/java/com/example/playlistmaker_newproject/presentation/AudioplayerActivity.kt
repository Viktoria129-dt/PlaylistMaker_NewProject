package com.example.playlistmaker_newproject.presentation

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Looper.prepare
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker_newproject.R
import com.example.playlistmaker_newproject.di.Creator
import com.example.playlistmaker_newproject.domain.api.PlayerInteractor
import com.example.playlistmaker_newproject.domain.models.Track
import com.example.playlistmaker_newproject.domain.models.formatTrackDuration
import java.util.Locale

class AudioplayerActivity : AppCompatActivity() {
    private lateinit var buttonPlay: ImageButton
    private lateinit var timeOfSong: TextView
    private lateinit var playerInteractor: PlayerInteractor
    private var mainThread: Handler? = null
    private lateinit var currentTrack: Track

    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            updateProgress()
            mainThread?.postDelayed(this, 300)
        }
    }

    private fun formatTime(milliseconds: Int): String {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
    }

    private fun updateProgress() {
        try {
            if (playerInteractor.getPlayerState() == PlayerInteractor.STATE_PLAYING) {
                val currentPosition = playerInteractor.getCurrentPosition()
                timeOfSong.text = formatTime(currentPosition)
            }
        } catch (e: Exception) {
            Log.e("PLAYER_DEBUG", "Error in updateProgress", e)
        }
    }

    private fun pausePlayer() {
        try {
            playerInteractor.pause()
            buttonPlay.setImageResource(R.drawable.vector)
            mainThread?.removeCallbacks(updateProgressRunnable)
        } catch (e: Exception) {
            Log.e("PLAYER_DEBUG", "Error in pausePlayer", e)
        }
    }

    private fun startPlayer() {
        try {
            playerInteractor.play()
            buttonPlay.setImageResource(R.drawable.pause_button)
            mainThread?.post(updateProgressRunnable)
        } catch (e: Exception) {
            Log.e("PLAYER_DEBUG", "Error in startPlayer", e)
        }
    }

    private fun playbackControl() {
        try {
            Log.d("PLAYER_DEBUG", "playbackControl called, state: ${playerInteractor.getPlayerState()}")

            when (playerInteractor.getPlayerState()) {
                PlayerInteractor.STATE_PLAYING -> {
                    Log.d("PLAYER_DEBUG", "Pausing player")
                    pausePlayer()
                }
                PlayerInteractor.STATE_PAUSED -> {
                    Log.d("PLAYER_DEBUG", "Resuming from paused")
                    startPlayer()
                }
                PlayerInteractor.STATE_PREPARED -> {
                    Log.d("PLAYER_DEBUG", "Starting from prepared")
                    startPlayer()
                }
                PlayerInteractor.STATE_DEFAULT -> {
                    Log.d("PLAYER_DEBUG", "Preparing track from default state")
                    prepareTrack()
                }
                else -> {
                    Log.d("PLAYER_DEBUG", "Unknown state, preparing track")
                    prepareTrack()
                }
            }
        } catch (e: Exception) {
            Log.e("PLAYER_DEBUG", "Error in playbackControl", e)
        }
    }

    private fun prepareTrack() {
        try {
            Log.d("PLAYER_DEBUG", "prepareTrack called")
            buttonPlay.isEnabled = false
            playerInteractor.prepare(
                currentTrack,
                onPrepared = {
                    Log.d("PLAYER_DEBUG", "Track prepared successfully")
                    buttonPlay.isEnabled = true
                    timeOfSong.text = "00:00"
                    startPlayer()
                },
                onCompletion = {
                    Log.d("PLAYER_DEBUG", "Playback completed")
                    buttonPlay.setImageResource(R.drawable.vector)
                    timeOfSong.text = "00:00"
                    mainThread?.removeCallbacks(updateProgressRunnable)
                }
            )
        } catch (e: Exception) {
            Log.e("PLAYER_DEBUG", "Error in prepareTrack", e)
            buttonPlay.isEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audioplayer)

        Log.d("PLAYER_DEBUG", "AudioplayerActivity onCreate started")

        try {
            // 1. Инициализация View элементов
            buttonPlay = findViewById<ImageButton>(R.id.buttonCenter)
            timeOfSong = findViewById<TextView>(R.id.timeOfSong)
            Log.d("PLAYER_DEBUG", "Views initialized")

            // 2. Инициализация Handler и Interactor
            mainThread = Handler(Looper.getMainLooper())
            playerInteractor = Creator.providePlayerInteractor()
            Log.d("PLAYER_DEBUG", "Handler and Interactor initialized")

            val buttonBackAudio = findViewById<ImageButton>(R.id.backAudio)
            buttonBackAudio.setOnClickListener {
                finish()
            }

            // 3. Получение трека из Intent
            val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("TRACK", Track::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra<Track>("TRACK")
            }

            if (track != null) {
                Log.d("PLAYER_DEBUG", "Track received: ${track.trackName}")
                currentTrack = track
                setupPlayerUI(track)

            } else {
                Log.e("PLAYER_DEBUG", "Track is null!")
                finish()
            }

            buttonPlay.setOnClickListener {
                Log.d("PLAYER_DEBUG", "Play button clicked")
                playbackControl()
            }

            Log.d("PLAYER_DEBUG", "AudioplayerActivity onCreate completed successfully")

        } catch (e: Exception) {
            Log.e("PLAYER_DEBUG", "Error in onCreate", e)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("PLAYER_DEBUG", "AudioplayerActivity onPause")
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("PLAYER_DEBUG", "AudioplayerActivity onDestroy")
        playerInteractor.release()
        mainThread?.removeCallbacks(updateProgressRunnable)
    }

    private fun setupPlayerUI(track: Track) {
        try {
            val albumImage = findViewById<ImageView>(R.id.beatlesImage)
            val trackName = findViewById<TextView>(R.id.NameOfSong)
            val authorName = findViewById<TextView>(R.id.nameOfAuthor)
            val timeOfTrack = findViewById<TextView>(R.id.timeNumbers)
            val albumName = findViewById<TextView>(R.id.nameOfAlbum)
            val yearRelease = findViewById<TextView>(R.id.yearNumbers)
            val genreOfSong = findViewById<TextView>(R.id.nameOfGenre)
            val country = findViewById<TextView>(R.id.nameOfCountry)

            trackName.text = track.trackName
            authorName.text = track.artistName
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

            Log.d("PLAYER_DEBUG", "UI setup completed")
        } catch (e: Exception) {
            Log.e("PLAYER_DEBUG", "Error in setupPlayerUI", e)
        }
    }

    private fun formatYear(releaseDate: String): String {
        return try {
            releaseDate.substring(0, 4)
        } catch (e: Exception) {
            releaseDate
        }
    }
}



