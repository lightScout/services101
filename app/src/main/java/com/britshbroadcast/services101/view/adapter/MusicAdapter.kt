package com.britshbroadcast.services101.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.britshbroadcast.services101.R
import com.britshbroadcast.services101.databinding.MusicItemLayoutBinding
import com.britshbroadcast.services101.model.Song
import com.squareup.picasso.Picasso

class MusicAdapter(private val playList: List<Song>, private val musicAdapterDelegate: MusicAdapterDelegate): RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {


    inner class MusicViewHolder(val binding: MusicItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    }

    interface MusicAdapterDelegate{
        fun selectMusic(song: Song)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding = MusicItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false )
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        with(holder){
            with(playList[position]){
                binding.songTitleTextView.text = this.songInfo
                
                
                Picasso.get()
                        .load(this.albumCover)
                        .placeholder(R.drawable.ic_album)
                        .into(binding.musicCoverImageView)
                
                
                holder.itemView.setOnClickListener {
                    musicAdapterDelegate.selectMusic(this)
                }

            }
        }
    }

    override fun getItemCount(): Int = playList.size


}