package com.example.playlistmaker_newproject.domain.api

interface ThemeRepository {
    fun saveDarkTheme(darkThemeEnabled: Boolean)
    fun getDarkTheme(): Boolean
    fun applyTheme(darkThemeEnabled: Boolean)
}