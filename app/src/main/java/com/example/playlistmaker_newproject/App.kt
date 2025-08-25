package com.example.playlistmaker_newproject

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {
    var darkTheme: Boolean = false

    companion object {
        const val PREFS_NAME = "app_setting"
        const val KEY_DARK_THEME = "dark_theme_key"
    }

    private fun saveThemeSettings(){
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        sharedPreferences.edit()
            .putBoolean(KEY_DARK_THEME, darkTheme)
            .apply()
    }


    override fun onCreate(){
        super.onCreate()
        loadThemeSettings()
        applyTheme()

    }

    private fun applyTheme(){
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme){
                AppCompatDelegate.MODE_NIGHT_YES
            }
            else{
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun loadThemeSettings(){
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(KEY_DARK_THEME, false)
    }


    fun SwitchTheme(darkThemeEnabled: Boolean){
        darkTheme = darkThemeEnabled
        saveThemeSettings()
        applyTheme()


    }
}