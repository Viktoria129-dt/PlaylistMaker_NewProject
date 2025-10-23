package com.example.playlistmaker_newproject.data.network

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker_newproject.domain.api.ThemeRepository

class ThemeRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : ThemeRepository {

    companion object {
        const val KEY_DARK_THEME = "dark_theme_key"
    }

    override fun saveDarkTheme(darkThemeEnabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_DARK_THEME, darkThemeEnabled)
            .apply()
    }

    override fun getDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_THEME, false)
    }

    override fun applyTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}