package com.example.playlistmaker_newproject.domain.api

interface ThemeInteractor {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun isDarkTheme(): Boolean
    fun applyTheme()
}