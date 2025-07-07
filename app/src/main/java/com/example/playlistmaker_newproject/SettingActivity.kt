package com.example.playlistmaker_newproject

import android.content.Intent
import android.net.Uri
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingActivity : androidx.appcompat.app.AppCompatActivity() {
    @android.annotation.SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.playlistmaker_newproject.R.layout.activity_settings)
        val backButton = findViewById<android.widget.ImageButton>(com.example.playlistmaker_newproject.R.id.backButton)
        backButton.setOnClickListener{
            finish()
        }
    }
}