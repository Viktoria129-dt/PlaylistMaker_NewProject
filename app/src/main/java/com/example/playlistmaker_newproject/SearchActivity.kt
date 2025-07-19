package com.example.playlistmaker_newproject

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SearchActivity : AppCompatActivity() {
    private var savedText:String=""
    private lateinit var searchLine: android.widget.EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        if (savedInstanceState != null) {
            savedText = savedInstanceState.getString("SAVED_TEXT") ?: ""
        }

        val buttonBack = findViewById<com.google.android.material.appbar.MaterialToolbar>(com.example.playlistmaker_newproject.R.id.bttnBack)
        buttonBack.setOnClickListener{
            finish()
        }

        searchLine = findViewById<android.widget.EditText>(com.example.playlistmaker_newproject.R.id.searchLine)
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

        val recyclerView = findViewById<RecyclerView>(R.id.track)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val trackAdapter = TrackAdapter(
            listOf(
                Track("Smells Like Teen Spirit","Nirvana","5:01","https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"),
                Track("Billie Jean", "Michael Jackson", "4:35", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
                Track("Stayin' Alive","Bee Gees","4:10","https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
                Track("Whole Lotta Love","Led Zeppelin","5:33","https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"),
                Track("Sweet Child O'Mine","Guns N' Roses","5:03","https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg")

            )
        )
        recyclerView.adapter = trackAdapter
    }
    override fun onSaveInstanceState(outState: android.os.Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SAVED TEXT", savedText)
    }

    override fun onRestoreInstanceState(savedInstanceState: android.os.Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val restoredText = savedInstanceState.getString("SAVED TEXT")
        searchLine.setText(restoredText)
    }

}

data class Track(
    val trackName:String,
    val artistName:String,
    val trackTime:String,
    val artworkUrl100:String
)


class TrackHolder(itemView:View): RecyclerView.ViewHolder(itemView){

    private val trackName: TextView = itemView.findViewById(R.id.songTitle)
    private val artistName: TextView = itemView.findViewById(R.id.bandName)
    private val trackTime: TextView = itemView.findViewById(R.id.timeSong)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.facebookImage)


    fun bind(item: Track){
        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = item.trackTime
        Glide.with(itemView.context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .onlyRetrieveFromCache(false)
            .transform(RoundedCorners(10))
            .into(artworkUrl100)
    }

}

class TrackAdapter(
    private val data: List<Track>): RecyclerView.Adapter<TrackHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_one_item,parent,false)
        return TrackHolder(view)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(data[position])
    }
    override fun getItemCount(): Int {
        return data.size
    }
}