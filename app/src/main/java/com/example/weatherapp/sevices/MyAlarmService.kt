package com.example.weatherapp.sevices

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.IBinder
import com.example.weatherapp.R

class MyAlarmService : Service() {
    private lateinit var mediaPlayer : MediaPlayer
    override fun onBind(intent: Intent): IBinder? {
       return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone1)
        mediaPlayer.start()
        return START_STICKY
    }
    override fun onDestroy() {
        mediaPlayer.stop()
        super.onDestroy()
    }
}