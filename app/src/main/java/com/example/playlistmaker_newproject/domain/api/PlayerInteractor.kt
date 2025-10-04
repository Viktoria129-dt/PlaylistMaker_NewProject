package com.example.playlistmaker_newproject.domain.api

import com.example.playlistmaker_newproject.domain.models.Track

interface PlayerInteractor {
    fun prepare(track: Track, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun play()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
    fun getPlayerState(): Int

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}