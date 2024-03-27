package com.example.weatherapp.sevices

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.language
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.views.home.HomeFragment
import com.example.weatherapp.workers.AlertWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DismissNotificationReceiver  : BroadcastReceiver(){
    private lateinit var appRepo:AppRepository
    private lateinit var weatherData : WeatherResponse
    override fun onReceive(context: Context?, intent: Intent) {
//        val stopIntent = Intent(context, MyAlarmService::class.java)
//        context?.stopService(stopIntent)
        val id = intent.getIntExtra("id",0)
        val lat = intent.getDoubleExtra("lat",0.0)
        val long = intent.getDoubleExtra("long",0.0)
        appRepo = AppRepository.getInstance(context!!)
        CoroutineScope(Dispatchers.IO).launch{
            appRepo.deleteAlertById(id)
            getCurrentWeather(lat,long)
            val current = weatherData.current
            showNotification(context, "Weather alert", "The weather in ${weatherData.timezone} is ${current.weather[0].main}\ntemp is ${current.temp}°C")
            Log.i("TAG", "onReceive: heeeeeeeee ${intent.extras}")
        }
    }
    private fun showNotification(context: Context, title: String, content: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("Alert", "Alert")
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(context, AlertWorker.CHANNEL_ID)
            .setSmallIcon(R.drawable.cloud_2489384) // Update with your correct icon resource
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alerts"
            val descriptionText = "Allows the application to send you alert notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(AlertWorker.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(AlertWorker.NOTIFICATION_ID, builder.build())

    }
    private suspend fun getCurrentWeather(lat:Double, long:Double)
    {
        Log.i("TAG", "getCurrentWeather: $language")
        appRepo.getAllForecastData(lat,long, HomeFragment.API_KEY, lang = language ).collect(){
            weatherData=it
        }
    }
    private suspend fun deleteWithId(id:Int)
    {
        appRepo.deleteAlertById(id)
    }
}