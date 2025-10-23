package com.example.playlistmaker_newproject.domain.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker_newproject.domain.api.ThemeInteractor
import com.example.playlistmaker_newproject.domain.api.ThemeRepository

class ThemeInteractorImpl(
    private val themeRepository: ThemeRepository
) : ThemeInteractor {

    override fun switchTheme(darkThemeEnabled: Boolean) {
        themeRepository.saveDarkTheme(darkThemeEnabled)
        themeRepository.applyTheme(darkThemeEnabled)
    }

    override fun isDarkTheme(): Boolean {
        return themeRepository.getDarkTheme()
    }

    override fun applyTheme() {
        themeRepository.applyTheme(isDarkTheme())
    }
}