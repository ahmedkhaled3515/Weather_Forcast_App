package com.example.weatherapp.sevices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.weatherapp.NOTIFICATION_ID

class DismissReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.i("TAG", "onReceive: dismiss")
        val serviceIntent = Intent(context, MyAlarmService::class.java)
        context.stopService(serviceIntent)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(NOTIFICATION_ID)

    }
}