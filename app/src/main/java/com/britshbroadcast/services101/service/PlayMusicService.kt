package com.britshbroadcast.services101.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.britshbroadcast.services101.R
import com.britshbroadcast.services101.model.Song

class PlayMusicService: Service() {

    val TAG = "TAG_J"
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
        mediaPlayer = MediaPlayer.create(this, R.raw.gotta_use_my_time)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")

//        mediaPlayer.prepare()
//        mediaPlayer.start()


        return super.onStartCommand(intent, flags, startId)

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    fun chooseSong(song: Song){


            //mediaPLayer.setDataSource(uri/ path/ etc)
        mediaPlayer.reset()
            mediaPlayer = MediaPlayer.create(this, song.songResource)
            mediaPlayer.start()

        createNotificationChannel(song)
    }

    private lateinit var  notificationManager: NotificationManager
    private fun createNotificationChannel(song: Song) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channel =   NotificationChannel(
                        "MY_NOTIFICATION_ID",
                        "com.brithishbroadcast",
                        NotificationManager.IMPORTANCE_LOW

                )

                notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
        val notification = NotificationCompat.Builder(this, "MY_NOTIFICATION_ID")
                .setSmallIcon(R.drawable.ic_album)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.flame))
                .setContentTitle(song.songInfo)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build()

        //Foreground service also has to have an idea
        startForeground(222, notification)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(222, notification)
        }


    }



    override fun onTaskRemoved(rootIntent: Intent?) {
        if(mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            stopForeground(Service.STOP_FOREGROUND_REMOVE)
        }
        stopSelf()

        super.onTaskRemoved(rootIntent)

    }

    fun playSong(){
        if(!mediaPlayer.isPlaying)
            mediaPlayer.start()

    }

    fun pauseSong(){
        if(mediaPlayer.isPlaying)
            mediaPlayer.pause()
    }



    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG, "onBind: ")
        return PlayBinder()
    }

    inner class PlayBinder: Binder() {
        fun getPlayInstance(): PlayMusicService = this@PlayMusicService
    }
}