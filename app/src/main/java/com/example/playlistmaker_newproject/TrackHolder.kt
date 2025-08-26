package com.example.playlistmaker_newproject

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackHolder(itemView:View): RecyclerView.ViewHolder(itemView){

    private val trackName: TextView = itemView.findViewById(R.id.songTitle)
    private val artistName: TextView = itemView.findViewById(R.id.bandName)
    private val trackTime: TextView = itemView.findViewById(R.id.timeSong)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.facebookImage)
    private val collectionName: TextView = itemView.findViewById(R.id.nameOfAlbom)
    private val releaseDate: TextView = itemView.findViewById(R.id.year)
    private val primaryGenreName: TextView = itemView.findViewById(R.id.nameOfGenre)
    private val country: TextView = itemView.findViewById(R.id.country)



    fun bind(item: Track){
        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = formatTrackDuration(item.trackTimeMillis)
        collectionName.text = item.collectionName
        releaseDate.text = item.collectionName
        primaryGenreName.text = item.primaryGenreName
        country.text = item.country

        Glide.with(itemView.context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .onlyRetrieveFromCache(false)
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(artworkUrl100)
    }

}

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics).toInt()
}