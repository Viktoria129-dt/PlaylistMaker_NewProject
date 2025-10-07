package com.example.playlistmaker_newproject.presentation

import android.app.Application
import com.example.playlistmaker_newproject.di.Creator
import com.example.playlistmaker_newproject.domain.api.ThemeInteractor

class App : Application() {

    private lateinit var themeInteractor: ThemeInteractor

    override fun onCreate() {
        super.onCreate()

        themeInteractor = Creator.provideThemeInteractor(this)
        themeInteractor.applyTheme()
    }
    fun SwitchTheme(darkThemeEnabled: Boolean) {
        themeInteractor.switchTheme(darkThemeEnabled)
    }

    val darkTheme: Boolean
        get() = themeInteractor.isDarkTheme()
}