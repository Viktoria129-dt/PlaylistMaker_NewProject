package com.example.playlistmaker_newproject

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


import org.w3c.dom.Text

class TrackHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

    private val isPlayerLayout: Boolean = viewType == TrackAdapter.VIEW_TYPE_PLAYER

    // Обязательные поля
    private val trackName: TextView = itemView.findViewById(R.id.NameOfSong)
    private val artistName: TextView = itemView.findViewById(R.id.nameOfAuthor)
    private val trackTime: TextView = itemView.findViewById(R.id.timeOfSong)
    private val artwork: ImageView = itemView.findViewById(R.id.beatlesImage)

    // Опциональные поля
    private val collectionName: TextView? = if (isPlayerLayout) {
        Log.d("TRACK_HOLDER", "Player layout - initializing collectionName")
        itemView.findViewById(R.id.nameOfAlbum)
    } else {
        Log.d("TRACK_HOLDER", "List layout - collectionName will be null")
        null
    }

    private val releaseDate: TextView? = if (isPlayerLayout) {
        Log.d("TRACK_HOLDER", "Player layout - initializing releaseDate")
        itemView.findViewById(R.id.yearNumbers)
    } else {
        Log.d("TRACK_HOLDER", "List layout - releaseDate will be null")
        null
    }

    private val primaryGenreName: TextView? = if (isPlayerLayout) {
        Log.d("TRACK_HOLDER", "Player layout - initializing primaryGenreName")
        itemView.findViewById(R.id.nameOfGenre)
    } else {
        Log.d("TRACK_HOLDER", "List layout - primaryGenreName will be null")
        null
    }

    private val country: TextView? = if (isPlayerLayout) {
        Log.d("TRACK_HOLDER", "Player layout - initializing country")
        itemView.findViewById(R.id.nameOfCountry)
    } else {
        Log.d("TRACK_HOLDER", "List layout - country will be null")
        null
    }

    init {
        Log.d("TRACK_HOLDER", "=== NEW TRACKHOLDER CREATED ===")
        Log.d("TRACK_HOLDER", "viewType: $viewType")
        Log.d("TRACK_HOLDER", "isPlayerLayout: $isPlayerLayout")
        Log.d("TRACK_HOLDER", "VIEW_TYPE_PLAYER constant: ${TrackAdapter.VIEW_TYPE_PLAYER}")
        Log.d("TRACK_HOLDER", "VIEW_TYPE_LIST constant: ${TrackAdapter.VIEW_TYPE_LIST}")
    }

    fun bind(item: Track) {
        Log.d("TRACK_HOLDER", "=== BINDING TRACK ===")
        Log.d("TRACK_HOLDER", "Track: ${item.trackName} by ${item.artistName}")
        Log.d("TRACK_HOLDER", "isPlayerLayout in bind: $isPlayerLayout")

        trackName.text = item.trackName ?: ""
        artistName.text = item.artistName ?: ""
        trackTime.text = formatTrackDuration(item.trackTimeMillis ?: 0)

        if (isPlayerLayout) {
            Log.d("TRACK_HOLDER", "Binding player-specific fields")
            collectionName?.text = item.collectionName ?: "No album"
            releaseDate?.text = item.releaseDate?.take(4) ?: "No date"
            primaryGenreName?.text = item.primaryGenreName ?: "No genre"
            country?.text = item.country ?: "No country"

            Log.d("TRACK_HOLDER", "Album: ${item.collectionName}")
            Log.d("TRACK_HOLDER", "Release date: ${item.releaseDate}")
            Log.d("TRACK_HOLDER", "Genre: ${item.primaryGenreName}")
            Log.d("TRACK_HOLDER", "Country: ${item.country}")
        } else {
            Log.d("TRACK_HOLDER", "Skipping player-specific fields (list layout)")
        }

        Log.d("TRACK_HOLDER", "Original artwork URL: ${item.artworkUrl100}")
        loadArtwork(item.artworkUrl100, isPlayerLayout)
    }

    private fun loadArtwork(artworkUrl: String?, isPlayer: Boolean) {
        Log.d("TRACK_HOLDER", "=== LOADING ARTWORK ===")
        Log.d("TRACK_HOLDER", "isPlayer parameter: $isPlayer")
        Log.d("TRACK_HOLDER", "artworkUrl: $artworkUrl")

        if (artworkUrl == null) {
            Log.d("TRACK_HOLDER", "Artwork URL is null, using placeholder")
            artwork.setImageResource(R.drawable.placeholder_audio)
            return
        }

        val imageUrl = if (isPlayer) {
            Log.d("TRACK_HOLDER", "Loading HIGH resolution for player")
            val highResUrl = getHighResolutionArtworkUrl(artworkUrl)
            Log.d("TRACK_HOLDER", "High resolution URL: $highResUrl")
            highResUrl
        } else {
            Log.d("TRACK_HOLDER", "Loading STANDARD resolution for list")
            Log.d("TRACK_HOLDER", "Standard URL: $artworkUrl")
            artworkUrl
        }

        Log.d("TRACK_HOLDER", "Final URL to load: $imageUrl")

        Glide.with(itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_audio)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(artwork)

        Log.d("TRACK_HOLDER", "Glide load initiated")
    }


    private fun getHighResolutionArtworkUrl(originalUrl: String): String {
        return originalUrl.replaceAfterLast('/', "512x512bb.jpg")
    }

    private fun formatTrackDuration(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val formatted = String.format("%d:%02d", minutes, seconds)
        Log.d("TRACK_HOLDER", "Formatted duration: $millis ms -> $formatted")
        return formatted
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        val pixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
        Log.d("TRACK_HOLDER", "Converted $dp dp to $pixels px")
        return pixels
    }
}