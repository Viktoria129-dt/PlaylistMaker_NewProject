package com.example.playlistmaker_newproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackAdapter(
    private val tracks: List<Track>,
    private val isPlayerLayout: Boolean = false,
    private val onItemClick: ((Track) -> Unit)? = null
) : RecyclerView.Adapter<TrackHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (isPlayerLayout) VIEW_TYPE_PLAYER else VIEW_TYPE_LIST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val layoutRes = when (viewType) {
            VIEW_TYPE_PLAYER -> R.layout.audioplayer
            VIEW_TYPE_LIST -> R.layout.track_one_item
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return TrackHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(tracks[position])
        }
    }

    override fun getItemCount(): Int = tracks.size

    companion object {
        const val VIEW_TYPE_LIST = 0
        const val VIEW_TYPE_PLAYER = 1
    }
}