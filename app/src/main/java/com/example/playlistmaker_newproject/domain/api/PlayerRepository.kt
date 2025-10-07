package com.example.playlistmaker_newproject.domain.api

interface PlayerRepository {
    fun prepare(trackUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun start()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
}