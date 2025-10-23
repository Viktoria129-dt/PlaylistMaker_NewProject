package com.example.playlistmaker_newproject.domain.impl

import android.media.MediaPlayer
import com.example.playlistmaker_newproject.domain.api.PlayerRepository

class PlayerRepositoryImpl : PlayerRepository {

    private val mediaPlayer = MediaPlayer()
    private var isPrepared = false

    override fun prepare(
        trackUrl: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            isPrepared = true
            onPrepared()
        }

        mediaPlayer.setOnCompletionListener {
            onCompletion()
        }
    }

    override fun start() {
        if (isPrepared) {
            mediaPlayer.start()
        }
    }

    override fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun release() {
        mediaPlayer.release()
        isPrepared = false
    }

    override fun getCurrentPosition(): Int {
        return if (isPrepared) mediaPlayer.currentPosition else 0
    }
}