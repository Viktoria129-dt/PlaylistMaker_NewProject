package com.example.playlistmaker_newproject.domain.impl

import android.media.MediaPlayer
import com.example.playlistmaker_newproject.domain.api.PlayerInteractor
import com.example.playlistmaker_newproject.domain.api.PlayerInteractor.Companion.STATE_DEFAULT
import com.example.playlistmaker_newproject.domain.api.PlayerInteractor.Companion.STATE_PAUSED
import com.example.playlistmaker_newproject.domain.api.PlayerInteractor.Companion.STATE_PLAYING
import com.example.playlistmaker_newproject.domain.api.PlayerInteractor.Companion.STATE_PREPARED
import com.example.playlistmaker_newproject.domain.models.Track

class PlayerInteractorImpl : PlayerInteractor {

    private val mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    override fun prepare(track: Track, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            onCompletion()
        }
    }

    override fun play() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun release() {
        mediaPlayer.release()
        playerState = STATE_DEFAULT
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getPlayerState(): Int {
        return playerState
    }
}