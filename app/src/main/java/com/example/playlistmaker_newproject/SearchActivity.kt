package com.example.playlistmaker_newproject

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {
    private var savedText:String=""
    private lateinit var editText: android.widget.EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        val buttonBack = findViewById<com.google.android.material.appbar.MaterialToolbar>(com.example.playlistmaker_newproject.R.id.bttnBack)
        buttonBack.setOnClickListener{
            finish()
        }

        val searchLine = findViewById<android.widget.EditText>(com.example.playlistmaker_newproject.R.id.searchLine)
        val clearButton = findViewById<android.widget.ImageView>(com.example.playlistmaker_newproject.R.id.clearIcon)
        clearButton.setOnClickListener{
            searchLine.text.clear()
            val inputMethodManager = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? android.view.inputmethod.InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchLine.windowToken, 0)
        }

        val textWatcher1 = object: android.text.TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = if (p0.isNullOrEmpty()) android.view.View.GONE else android.view.View.VISIBLE
            }
            override fun afterTextChanged(p0: android.text.Editable?) {

            }
        }
        searchLine.addTextChangedListener(textWatcher1)

        val textWatcher2 = object : android.text.TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                savedText = p0?.toString()?:""
            }

            override fun afterTextChanged(p0: android.text.Editable?) {

            }
        }
        searchLine.addTextChangedListener(textWatcher2)
    }
    override fun onSaveInstanceState(outState: android.os.Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SAVED TEXT", savedText)
    }

    override fun onRestoreInstanceState(savedInstanceState: android.os.Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val restoredText = savedInstanceState.getString("SAVED TEXT")
        editText.setText(restoredText)
    }

}