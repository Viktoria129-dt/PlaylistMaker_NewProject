package com.example.playlistmaker_newproject.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker_newproject.R
import com.example.playlistmaker_newproject.di.Creator
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener{
            finish()
        }
        val sharebutton = findViewById<ImageButton>(R.id.shareButton)
        sharebutton.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.letsLaunchApp))
            startActivity(shareIntent)
        }

        val supportButton = findViewById<ImageButton>(R.id.supportButton)
        supportButton.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf("Vikkipinya@gmail.com")
            )
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.MessagetoDevelopers))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.ThanksDevelopers))
            startActivity(supportIntent)
        }

        val buttonRight = findViewById<ImageButton>(R.id.buttonRight)
        buttonRight.setOnClickListener{
            val rightIntent = Intent(Intent.ACTION_VIEW)
            rightIntent.data = Uri.parse(getString(R.string.linkAndroidDeveloper))
            startActivity(rightIntent)
        }


        val switcher = findViewById<SwitchMaterial>(R.id.switcher)
        switcher.isChecked = Creator.provideThemeInteractor(this).isDarkTheme()
        switcher.setOnCheckedChangeListener { _, checked ->
            Creator.provideThemeInteractor(this).switchTheme(checked)
        }

    }
}