package com.example.playlistmaker_newproject.domain.impl

import android.media.MediaPlayer
import com.example.playlistmaker_newproject.domain.api.PlayerInteractor
import com.example.playlistmaker_newproject.domain.api.PlayerInteractor.Companion.STATE_DEFAULT
import com.example.playlistmaker_newproject.domain.api.PlayerInteractor.Companion.STATE_PAUSED
import com.example.playlistmaker_newproject.domain.api.PlayerInteractor.Companion.STATE_PLAYING
import com.example.playlistmaker_newproject.domain.api.PlayerInteractor.Companion.STATE_PREPARED
import com.example.playlistmaker_newproject.domain.api.PlayerRepository
import com.example.playlistmaker_newproject.domain.models.Track

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {


    private var playerState = STATE_DEFAULT

    override fun prepare(track: Track, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        playerState = STATE_DEFAULT
        playerRepository.prepare(track.previewUrl,
            onPrepared = {
                playerState = STATE_PREPARED
                onPrepared()
            },
            onCompletion = {
                playerState = STATE_PREPARED
                onCompletion()
            }
        )
    }

    override fun play() {
        playerRepository.start()
        playerState = STATE_PLAYING
    }

    override fun pause() {
        playerRepository.pause()
        playerState = STATE_PAUSED
    }

    override fun release() {
        playerRepository.release()
        playerState = STATE_DEFAULT
    }

    override fun getCurrentPosition(): Int {
        return playerRepository.getCurrentPosition()
    }

    override fun getPlayerState(): Int {
        return playerState
    }
}