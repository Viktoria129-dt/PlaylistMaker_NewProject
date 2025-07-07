package com.example.playlistmaker_newproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.firstButton)
        val searchButtonClickListener:View.OnClickListener = object: View.OnClickListener{
            override fun onClick(v: View?){
                val searchActivity = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchActivity)
            }
        }
        searchButton.setOnClickListener(searchButtonClickListener)


        val mediaButton = findViewById<Button>(R.id.secondButton)
        mediaButton.setOnClickListener{
            val mediaActivity = Intent(this, MediatekaActivity::class.java)
            startActivity(mediaActivity)
        }

        val settingButton = findViewById<Button>(R.id.thirdButton)
        settingButton.setOnClickListener{
            val settingActivity = Intent(this, SettingActivity::class.java)
            startActivity(settingActivity)
        }


    }
}