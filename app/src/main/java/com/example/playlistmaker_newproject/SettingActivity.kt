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
        val sharebutton = findViewById<android.widget.ImageButton>(com.example.playlistmaker_newproject.R.id.shareButton)
        sharebutton.setOnClickListener{
            val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Скачайте это крутое приложение: 'https://practicum.yandex.ru/android-developer/'")
            startActivity(shareIntent)
        }

        val supportButton = findViewById<android.widget.ImageButton>(com.example.playlistmaker_newproject.R.id.supportButton)
        supportButton.setOnClickListener{
            val supportIntent = android.content.Intent(android.content.Intent.ACTION_SENDTO)
            supportIntent.data = android.net.Uri.parse("mailto:")
            supportIntent.putExtra(
                android.content.Intent.EXTRA_EMAIL,
                arrayOf("Vikkipinya@gmail.com")
            )
            supportIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Сообщение разработчикам и разработчицам приложения Playlist Maker")
            supportIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Спасибо разработчикам и разработчицам за крутое приложение!")
            startActivity(supportIntent)
        }

        val buttonRight = findViewById<android.widget.ImageButton>(com.example.playlistmaker_newproject.R.id.buttonRight)
        buttonRight.setOnClickListener{
            val rightIntent = android.content.Intent(android.content.Intent.ACTION_VIEW)
            rightIntent.data = android.net.Uri.parse("https://yandex.ru/legal/practicum_offer/")
            startActivity(rightIntent)
        }
    }
}