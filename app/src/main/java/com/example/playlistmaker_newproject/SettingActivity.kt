package com.example.playlistmaker_newproject

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial

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
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.letsLaunchApp))
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
            supportIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.MessagetoDevelopers))
            supportIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.ThanksDevelopers))
            startActivity(supportIntent)
        }

        val buttonRight = findViewById<android.widget.ImageButton>(com.example.playlistmaker_newproject.R.id.buttonRight)
        buttonRight.setOnClickListener{
            val rightIntent = android.content.Intent(android.content.Intent.ACTION_VIEW)
            rightIntent.data = android.net.Uri.parse(getString(R.string.linkAndroidDeveloper))
            startActivity(rightIntent)
        }


        val switcher = findViewById<SwitchMaterial>(R.id.switcher)
        switcher.isChecked = (applicationContext as App).darkTheme
        switcher.setOnCheckedChangeListener{ switcher, checked ->
            (applicationContext as App).SwitchTheme(checked)
        }

    }
}
