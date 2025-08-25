package com.example.playlistmaker_newproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackAdapter(
    private val tracks: List<Track>,
    private val onItemClick: ((Track)-> Unit)? = null): RecyclerView.Adapter<TrackHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_one_item,parent,false)
        return TrackHolder(view)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(tracks[position])
        }
    }
    override fun getItemCount(): Int {
        return tracks.size
    }
}