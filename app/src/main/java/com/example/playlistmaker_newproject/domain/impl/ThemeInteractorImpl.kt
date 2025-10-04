package com.example.playlistmaker_newproject.domain.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker_newproject.domain.api.ThemeInteractor

class ThemeInteractorImpl(
    private val sharedPreferences: SharedPreferences
) : ThemeInteractor {

    companion object {
        const val PREFS_NAME = "app_setting"
        const val KEY_DARK_THEME = "dark_theme_key"
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_DARK_THEME, darkThemeEnabled)
            .apply()
        applyTheme(darkThemeEnabled)
    }

    override fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_THEME, false)
    }

    override fun applyTheme() {
        applyTheme(isDarkTheme())
    }

    private fun applyTheme(darkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}