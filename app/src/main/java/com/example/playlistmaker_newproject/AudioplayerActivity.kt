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
import com.example.playlistmaker_newproject.di.Creator
import com.example.playlistmaker_newproject.domain.api.PlayerInteractor
import com.example.playlistmaker_newproject.domain.models.Track
import com.example.playlistmaker_newproject.domain.models.formatTrackDuration
import java.util.Locale

class AudioplayerActivity : androidx.appcompat.app.AppCompatActivity() {
    private lateinit var buttonPlay: ImageButton
    private lateinit var timeOfSong: TextView
    private lateinit var playerInteractor: PlayerInteractor
    private var mainTread: Handler? = null

    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            updateProgress()
            mainTread?.postDelayed(this, 300)
        }
    }

    private fun formatTime(milliseconds: Int): String {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
    }

    private fun updateProgress() {
        // ИСПРАВИТЬ: использовать playerInteractor вместо playerState
        if (playerInteractor.getPlayerState() == PlayerInteractor.STATE_PLAYING) {
            val currentPosition = playerInteractor.getCurrentPosition()
            timeOfSong.text = formatTime(currentPosition)
        }
    }

    private fun pausePlayer() {
        // ИСПРАВИТЬ: использовать playerInteractor вместо mediaPlayer
        playerInteractor.pause()
        buttonPlay.setImageResource(R.drawable.vector)
        mainTread?.removeCallbacks(updateProgressRunnable)
    }

    private fun startPlayer() {
        // ИСПРАВИТЬ: использовать playerInteractor вместо mediaPlayer
        playerInteractor.play()
        buttonPlay.setImageResource(R.drawable.pause_button)
        mainTread?.post(updateProgressRunnable)
    }

    private fun playbackControl() {
        // ИСПРАВИТЬ: использовать playerInteractor вместо playerState
        when (playerInteractor.getPlayerState()) {
            PlayerInteractor.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerInteractor.STATE_PAUSED -> {
                startPlayer()
            }
            PlayerInteractor.STATE_PREPARED -> {
                startPlayer()
            }
        }
    }

    private lateinit var currentTrack: Track

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audioplayer)

        // ДОБАВИТЬ: инициализация интерактора
        playerInteractor = Creator.providePlayerInteractor()

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

        if (track != null) {
            currentTrack = track
            setupPlayerUI(track)

            // ИСПРАВИТЬ: использовать playerInteractor вместо preparePlayer()
            playerInteractor.prepare(
                track,
                onPrepared = {
                    buttonPlay.isEnabled = true
                    timeOfSong.text = "00:00"
                },
                onCompletion = {
                    buttonPlay.setImageResource(R.drawable.vector)
                    timeOfSong.text = "00:00"
                    mainTread?.removeCallbacks(updateProgressRunnable)
                }
            )
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
        // ИСПРАВИТЬ: использовать playerInteractor вместо mediaPlayer
        playerInteractor.release()
        mainTread?.removeCallbacks(updateProgressRunnable)
    }

    private fun setupPlayerUI(track: Track) {
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

