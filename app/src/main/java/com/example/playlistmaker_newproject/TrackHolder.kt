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




    fun bind(item: Track){
        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = item.trackTimeMillis
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