package com.example.playlistmaker_newproject

import android.R.attr.level
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
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
import com.example.playlistmaker_newproject.MainActivity
import com.google.android.material.button.MaterialButton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class SearchActivity : AppCompatActivity() {
    private var savedText: String = ""
    var lastQuery: String = ""
    private lateinit var historySearch: SearchHistory
    private var historyResults = ArrayList<Track>()
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var clearButtonHistory: Button
    private lateinit var containerHistory: View
    private lateinit var searchResultsContainer: View
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var searchLine: android.widget.EditText
    private lateinit var errorState: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var emptyState: View
    private val tracks = ArrayList<Track>()



    fun checkHistoryV() {
        if (searchLine.text.isNullOrEmpty() && historyResults.isNotEmpty()) {
            containerHistory.visibility = View.VISIBLE
            searchResultsContainer.visibility = View.GONE
        } else {
            containerHistory.visibility = View.GONE
            searchResultsContainer.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showHistory() {
        val history = historySearch.getHistory()
        historyResults.clear()
        historyResults.addAll(history)
        historyAdapter.notifyDataSetChanged()

        if (history.isEmpty()) {
            containerHistory.visibility = View.GONE
            searchResultsContainer.visibility = View.VISIBLE
        } else {
            containerHistory.visibility = View.VISIBLE
            searchResultsContainer.visibility = View.GONE
        }
        checkHistoryV()
    }
    fun showEmptyState() {
        recyclerView.visibility = View.GONE
        errorState.visibility = View.GONE
        emptyState.visibility = View.VISIBLE
    }
    fun performSearch(query: String) {
        Log.d("SEARCH_DEBUG", "=== PERFORM SEARCH STARTED ===")
        Log.d("SEARCH_DEBUG", "Query: '$query'")
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        Log.d("SEARCH_DEBUG", "OkHttpClient created")
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val apiClient = retrofit.create(APIService::class.java)
        containerHistory.visibility = View.GONE
        searchResultsContainer.visibility = View.VISIBLE
        if (query.trim().isEmpty()) {
            showEmptyState()
            return
        }

        recyclerView.visibility = View.GONE
        errorState.visibility = View.GONE
        emptyState.visibility = View.GONE

        apiClient.search(query).enqueue(object : Callback<ResultsTracks> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultsTracks>,
                response: Response<ResultsTracks>
            ) {
                Log.d("SEARCH_DEBUG", "Response received: ${response.isSuccessful}")
                Log.d("SEARCH_DEBUG", "Response code: ${response.code()}")
                if (response.isSuccessful) {
                    response.body()?.let { results ->
                        Log.d("SEARCH_DEBUG", "Results count: ${results.results.size}")
                        if (results.results.isNotEmpty()) {
                            tracks.clear()
                            tracks.addAll(results.results)
                            trackAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            errorState.visibility = View.GONE
                            emptyState.visibility = View.GONE
                        } else {
                            Log.d("SEARCH_DEBUG", "No results found")
                            showEmptyState()
                        }
                    } ?: showEmptyState()
                } else {
                    Log.d("SEARCH_DEBUG", "Response not successful")
                    lastQuery = query
                    recyclerView.visibility = View.GONE
                    errorState.visibility = View.VISIBLE
                    emptyState.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ResultsTracks>, t: Throwable) {
                lastQuery = query
                recyclerView.visibility = View.GONE
                errorState.visibility = View.VISIBLE
                emptyState.visibility = View.GONE
                Log.e("SEARCH", "Search failed", t)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SEARCH_DEBUG", "onCreate started")
        setContentView(R.layout.activity_search)
        Log.d("SEARCH_DEBUG", "setContentView completed")

        searchLine = findViewById<android.widget.EditText>(com.example.playlistmaker_newproject.R.id.searchLine)
        errorState = findViewById(R.id.errorPlaceholder)
        emptyState = findViewById(R.id.stateEmpty)
        Log.d("SEARCH_DEBUG", "searchLine found: ${searchLine != null}")
        if (savedInstanceState != null) {
            savedText = savedInstanceState.getString("SAVED_TEXT") ?: ""
        }

        val buttonBack = findViewById<com.google.android.material.appbar.MaterialToolbar>(com.example.playlistmaker_newproject.R.id.bttnBack)
        buttonBack.setOnClickListener {
            finish()
        }
        recyclerView = findViewById<RecyclerView>(R.id.track)

        val clearButton = findViewById<android.widget.ImageView>(com.example.playlistmaker_newproject.R.id.clearIcon)
        clearButton.setOnClickListener {
            Log.d("SEARCH_DEBUG", "Clear button clicked - should clear text")
            searchLine.text.clear()
            tracks.clear()
            val inputMethodManager = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? android.view.inputmethod.InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchLine.windowToken, 0)
            recyclerView.visibility = View.GONE
            showHistory()
        }

        val textWatcher1 = object : android.text.TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                savedText = p0?.toString() ?: ""
                clearButton.visibility = if (p0.isNullOrEmpty()) android.view.View.GONE else android.view.View.VISIBLE

            }

            override fun afterTextChanged(p0: android.text.Editable?) {
            }
        }
        searchLine.addTextChangedListener(textWatcher1)

        trackAdapter = TrackAdapter(tracks, isPlayerLayout = false) { track ->
            if (track != null) {
                historySearch.addTrack(track)
                showHistory()
                val song = Intent(this@SearchActivity, AudioplayerActivity::class.java)
                song.putExtra("TRACK", track)
                startActivity(song)
            } else {
                Log.e("SEARCH", "Track is null! Cannot open player")
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter

        recyclerView.visibility = View.GONE
        errorState = findViewById<View>(R.id.errorPlaceholder)
        emptyState = findViewById<View>(R.id.stateEmpty)



        val buttonUpdate = findViewById<Button>(R.id.button_update)

        searchResultsContainer = findViewById<View>(R.id.searchResultsContainer)
        containerHistory = findViewById<View>(R.id.vgSearchHistory)

        buttonUpdate.setOnClickListener {
            if (lastQuery.isNotEmpty()) {
                performSearch(lastQuery)
            }
        }


        searchLine.setOnEditorActionListener { _, actionId, _ ->
            Log.d("SEARCH_DEBUG", "Editor action: $actionId, text: '${searchLine.text}'")
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d("SEARCH_DEBUG", "Calling performSearch with: '${searchLine.text}'")
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchLine.windowToken, 0)
                performSearch(searchLine.text.toString())
                true
            } else {
                Log.d("SEARCH_DEBUG", "Other action, not handled")
                false
            }

        }

        historySearch = SearchHistory(getSharedPreferences("Songs", MODE_PRIVATE))
        clearButtonHistory = findViewById<Button>(R.id.btnClearHistory)
        historyRecyclerView = findViewById<RecyclerView>(R.id.recyclerViewSearchHistory)

        historyAdapter = TrackAdapter(historyResults, isPlayerLayout = false) { track ->
            if (track != null) {
                historySearch.addTrack(track)
                showHistory()
                val song = Intent(this@SearchActivity, AudioplayerActivity::class.java)
                song.putExtra("TRACK", track)
                startActivity(song)
            } else {
                Log.e("SEARCH", "History track is null! Cannot open player")
            }
        }

        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = historyAdapter

        clearButtonHistory.setOnClickListener {
            historySearch.clearHistory()
            historyResults.clear()
            historyAdapter.notifyDataSetChanged()
            containerHistory.visibility = View.GONE
        }
        showHistory()

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