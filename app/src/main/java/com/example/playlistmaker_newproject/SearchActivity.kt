package com.example.playlistmaker_newproject

import android.R.attr.level
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class SearchActivity : AppCompatActivity() {
    private var savedText:String=""

    private lateinit var searchLine: android.widget.EditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchLine = findViewById<android.widget.EditText>(com.example.playlistmaker_newproject.R.id.searchLine)
        if (savedInstanceState != null) {
            savedText = savedInstanceState.getString("SAVED_TEXT") ?: ""
        }

        val buttonBack = findViewById<com.google.android.material.appbar.MaterialToolbar>(com.example.playlistmaker_newproject.R.id.bttnBack)
        buttonBack.setOnClickListener{
            finish()
        }


        val errorState = findViewById<LinearLayout>(R.id.errorPlaceholder)
        val emptyState = findViewById<LinearLayout>(R.id.stateEmpty)
        val recyclerView = findViewById<RecyclerView>(R.id.track)
        val tracks = ArrayList<Track>()

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

        val trackAdapter: TrackAdapter = TrackAdapter(tracks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        fun showEmptyState() {
            recyclerView.visibility = View.GONE
            errorState.visibility = View.GONE
            emptyState.visibility = View.VISIBLE
        }
        val apiClient = retrofit.create(APIService::class.java)

        fun performSearch(query: String) {
            if (query.trim().isEmpty()) {
                showEmptyState()
                return
            }

            Log.d("SEARCH", "Performing search for: $query")

            // Показываем индикатор загрузки
            recyclerView.visibility = View.GONE
            errorState.visibility = View.GONE
            emptyState.visibility = View.GONE
            // progressBar.visibility = View.VISIBLE  // добавьте ProgressBar в layout

            apiClient.search(query).enqueue(object : Callback<ResultsTracks> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ResultsTracks>,
                    response: Response<ResultsTracks>
                ) {


                    if (response.isSuccessful) {
                        response.body()?.let { results ->
                            if (results.results.isNotEmpty()) {
                                tracks.clear()
                                tracks.addAll(results.results)
                                trackAdapter.notifyDataSetChanged()
                                recyclerView.visibility = View.VISIBLE
                                errorState.visibility = View.GONE
                                emptyState.visibility = View.GONE
                            } else {
                                showEmptyState()
                            }
                        } ?: showEmptyState()
                    } else {
                        recyclerView.visibility = View.GONE
                        errorState.visibility = View.VISIBLE
                        emptyState.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<ResultsTracks>, t: Throwable) {
                    recyclerView.visibility = View.GONE
                    errorState.visibility = View.VISIBLE
                    emptyState.visibility = View.GONE
                    Log.e("SEARCH", "Search failed", t)
                }
            })
        }


        searchLine.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchLine.windowToken, 0)
                performSearch(searchLine.text.toString())
                true
            }
            else{
                false
            }

        }

    }


    override fun onSaveInstanceState(outState: android.os.Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SAVED_TEXT", savedText)
    }

    override fun onRestoreInstanceState(savedInstanceState: android.os.Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val restoredText = savedInstanceState.getString("SAVED_TEXT")
        searchLine.setText(restoredText)
    }
}










