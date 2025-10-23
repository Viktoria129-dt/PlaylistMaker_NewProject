package com.example.playlistmaker_newproject.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker_newproject.R
import com.example.playlistmaker_newproject.di.Creator
import com.example.playlistmaker_newproject.domain.api.SearchHistoryInteractor
import com.example.playlistmaker_newproject.domain.api.SearchInteractor
import com.example.playlistmaker_newproject.domain.models.Track
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
    private var savedText: String = ""
    var lastQuery: String = ""
    private var historyResults = ArrayList<Track>()
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var clearButtonHistory: Button
    private lateinit var containerHistory: View
    private lateinit var searchResultsContainer: View
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var searchLine: EditText
    private lateinit var errorState: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var emptyState: View
    private lateinit var searchInteractor: SearchInteractor
    private lateinit var searchHistoryInteractor: SearchHistoryInteractor
    private val tracks = ArrayList<Track>()

    private lateinit var progressBar: ProgressBar

    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable{performSearch(lastQuery)}

    private fun searchDebounce(query: String){
        handler.removeCallbacks(searchRunnable)
        lastQuery = query
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun CliclDebounce(): Boolean{
        val current = isClickAllowed
        if (current){
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
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
        val history = searchHistoryInteractor.getHistory()
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


        containerHistory.visibility = View.GONE
        searchResultsContainer.visibility = View.VISIBLE
        if (query.trim().isEmpty()) {
            showEmptyState()
            return
        }

        recyclerView.visibility = View.GONE
        errorState.visibility = View.GONE
        emptyState.visibility = View.GONE
        progressBar.visibility = View.VISIBLE


        searchInteractor.searchTracks(query, object : SearchInteractor.SearchCallback {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSuccess(results: List<Track>) {
                if (results.isNotEmpty()) {
                    tracks.clear()
                    tracks.addAll(results)
                    trackAdapter.notifyDataSetChanged()
                    recyclerView.visibility = View.VISIBLE
                    errorState.visibility = View.GONE
                    emptyState.visibility = View.GONE
                    progressBar.visibility = View.GONE
                } else {
                    showEmptyState()
                }
            }

            override fun onError(error: String) {
                lastQuery = query
                recyclerView.visibility = View.GONE
                errorState.visibility = View.VISIBLE
                emptyState.visibility = View.GONE
                progressBar.visibility = View.GONE
                Log.e("SEARCH", "Search failed: $error")
            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchInteractor = Creator.provideSearchInteractor(this)
        searchHistoryInteractor = Creator.provideSearchHistoryInteractor(this)
        searchLine = findViewById<EditText>(R.id.searchLine)
        errorState = findViewById(R.id.errorPlaceholder)
        emptyState = findViewById(R.id.stateEmpty)
        progressBar = findViewById<ProgressBar>(R.id.ProgressBar)
        if (savedInstanceState != null) {
            savedText = savedInstanceState.getString("SAVED_TEXT") ?: ""
        }


        val buttonBack = findViewById<MaterialToolbar>(R.id.bttnBack)
        buttonBack.setOnClickListener {
            finish()
        }
        recyclerView = findViewById<RecyclerView>(R.id.track)

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            searchLine.text.clear()
            tracks.clear()
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchLine.windowToken, 0)
            recyclerView.visibility = View.GONE
            showHistory()
        }

        val textWatcher1 = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                savedText = p0?.toString() ?: ""
                clearButton.visibility = if (p0.isNullOrEmpty()) View.GONE else View.VISIBLE
                searchDebounce(savedText)

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        searchLine.addTextChangedListener(textWatcher1)

        trackAdapter = TrackAdapter(tracks, isPlayerLayout = false) { track ->
            if (track != null && CliclDebounce()) {
                searchHistoryInteractor.addTrack(track)
                showHistory()
                val song = Intent(this@SearchActivity, AudioplayerActivity::class.java)
                song.putExtra("TRACK", track)
                startActivity(song)
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
            Log.d("SEARCH_DEBUG", "Editor action: $actionId")
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d("SEARCH_DEBUG", "Performing search...")
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchLine.windowToken, 0)
                performSearch(searchLine.text.toString())
                Log.d("SEARCH_DEBUG", "Search performed")
                false
            } else {
                false
            }
        }

        clearButtonHistory = findViewById<Button>(R.id.btnClearHistory)
        historyRecyclerView = findViewById<RecyclerView>(R.id.recyclerViewSearchHistory)

        historyAdapter = TrackAdapter(historyResults, isPlayerLayout = false) { track ->
            if (track != null) {
                searchHistoryInteractor.addTrack(track)
                showHistory()
                val song = Intent(this@SearchActivity, AudioplayerActivity::class.java)
                song.putExtra("TRACK", track)
                startActivity(song)
            } else {
            }
        }

        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = historyAdapter

        clearButtonHistory.setOnClickListener {
            searchHistoryInteractor.clearHistory()
            historyResults.clear()
            historyAdapter.notifyDataSetChanged()
            containerHistory.visibility = View.GONE
        }
        showHistory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SAVED_TEXT", savedText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val restoredText = savedInstanceState.getString("SAVED_TEXT")
        searchLine.setText(restoredText)
    }
}