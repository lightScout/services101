package com.britshbroadcast.services101

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.britshbroadcast.services101.databinding.ActivityMainBinding
import com.britshbroadcast.services101.model.Song
import com.britshbroadcast.services101.service.PlayMusicService
import com.britshbroadcast.services101.view.adapter.MusicAdapter
import com.britshbroadcast.services101.view.fragment.PlayerFragment
import java.util.concurrent.atomic.AtomicBoolean

class MainActivity : AppCompatActivity(), MusicAdapter.MusicAdapterDelegate, PlayerFragment.PlayInterface {

    private lateinit var playServiceIntent: Intent

    private val songList = mutableListOf<Song>(
        Song("Gotta use my time by Juan Silva", R.raw.gotta_use_my_time, "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2F3.bp.blogspot.com%2F_I6dhg-xn0JA%2FTKn9PSj6MkI%2FAAAAAAAAAJA%2F0NTaF0ykdRU%2Fs1600%2FMy%2Bfavourite%2Balbum%2Bcover.jpg&f=1&nofb=1"),
        Song("Rogue Valley by Coral Candle", R.raw.rogue_valley, "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fimages.vexels.com%2Fmedia%2Fusers%2F3%2F158737%2Fisolated%2Fpreview%2F3353b3a06bc810221952cccbbb189b47-grave-a-ilustra----o-de-vinil-raridade-by-vexels.png&f=1&nofb=1")
    )

    private lateinit var playService: PlayMusicService

    private var musicAdapter = MusicAdapter(songList, this)

    private val serviceConnection = object: ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            //Called when a the service has been successfully bound to
            playService = (binder as PlayMusicService.PlayBinder).getPlayInstance()

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
                //Called when an error occurs
        }

    }

    private lateinit var binding: ActivityMainBinding

    private var playerFragment = PlayerFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playServiceIntent = Intent(this,PlayMusicService::class.java)
        startService(playServiceIntent)//a started service can become a bound service

            //bind or creates/binds the service
            bindService(playServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)


        binding.songListRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.songListRecyclerView.adapter = musicAdapter
        
    }



    override fun onDestroy() {
        super.onDestroy()
        stopService(playServiceIntent)
        unbindService(serviceConnection)
    }

    override fun selectMusic(song: Song) {
        if(this::playService.isInitialized){
            playService.chooseSong(song)
            songIndex = songList.indexOf(song)
            playingBoolean.set(true)

            displayPlayFragment(song)

        }
    }

    private fun displayPlayFragment(song: Song) {
        var bundle = Bundle()
        bundle.putParcelable("SONG", song)
        playerFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                ).replace(R.id.main_frame, playerFragment)
                .addToBackStack(playerFragment.tag)
                .commit()


    }

    private var playingBoolean = AtomicBoolean(false)
    override fun pausePlayCurrentSong() {
        if(this::playService.isInitialized){
            if(playingBoolean.getAndSet(false)){
                playService.pauseSong()
            
            }
            else{
                playingBoolean.set(true)
                playService.playSong()
            }
        }
    }
    var songIndex = 0
    override fun nextSong() {
        if(this::playService.isInitialized){
            songIndex++
            if(songIndex == songList.size)
                songIndex = 0

            changeSong()

        }
    }

    override fun previousSong() {
        var albumCover: String = ""
        if(this::playService.isInitialized){
            songIndex--
            if(songIndex < 0)
                songIndex = songList.size - 1

            changeSong()
        }
    }

    private fun changeSong() {
        songList[songIndex].let {
            playService.chooseSong(it)
            playerFragment.changeSongCover(it)
            playingBoolean.set(true)
        }
    }

    override fun isPlaying(): Boolean = playingBoolean.get()
}