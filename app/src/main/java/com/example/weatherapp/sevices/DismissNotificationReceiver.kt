package com.example.weatherapp.sevices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DismissNotificationReceiver  : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val stopIntent = Intent(context, MyAlarmService::class.java)
        context?.stopService(stopIntent)

    }
}