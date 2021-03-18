package com.britshbroadcast.services101.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.britshbroadcast.services101.MainActivity
import com.britshbroadcast.services101.R
import com.britshbroadcast.services101.databinding.SongPlayFragmentBinding
import com.britshbroadcast.services101.model.Song
import com.squareup.picasso.Picasso
import java.util.concurrent.atomic.AtomicBoolean

class PlayerFragment: Fragment() {

    private lateinit var binding: SongPlayFragmentBinding

    interface PlayInterface{
        fun pausePlayCurrentSong()
        fun nextSong()
        fun previousSong()
        fun isPlaying(): Boolean
    }

    private lateinit var playInterface: PlayInterface


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SongPlayFragmentBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            nextImageView.setOnClickListener {
                playInterface.nextSong()
            }

            previousImageView.setOnClickListener {
                playInterface.previousSong()
            }

            playPauseImageView.setOnClickListener{
                playInterface.pausePlayCurrentSong()
                if(playInterface.isPlaying())
                playPauseImageView.setImageResource(R.drawable.ic_pause)
                else{
                    playPauseImageView.setImageResource(R.drawable.ic_play)
                }
            }

            arguments?.let {
                var song = it.get("SONG") as Song
               changeSongCover(song)
            }

        }


    }

    fun changeSongCover(song: Song){
        binding.songInfoTextView.text = song.songInfo

        Picasso.get()
                .load(song.albumCover)
                .placeholder(R.drawable.ic_album)
                .into(binding.songCoverImageView)

        binding.playPauseImageView.setImageResource(R.drawable.ic_pause)

    }

    //called when the activity has attached the fragment
    override fun onAttach(context: Context) {
        super.onAttach(context)
        playInterface = (context as MainActivity)
    }
}