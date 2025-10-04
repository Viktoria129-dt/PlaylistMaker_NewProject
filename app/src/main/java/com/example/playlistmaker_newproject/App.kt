package com.example.playlistmaker_newproject

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
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