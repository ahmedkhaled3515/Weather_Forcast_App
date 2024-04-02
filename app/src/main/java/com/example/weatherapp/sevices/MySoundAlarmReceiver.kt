package com.example.weatherapp.sevices

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.weatherapp.CHANNEL_ID
import com.example.weatherapp.MainActivity
import com.example.weatherapp.MainActivity2
import com.example.weatherapp.NOTIFICATION_ID
import com.example.weatherapp.R
import com.example.weatherapp.SettingsSharedPreferences
import com.example.weatherapp.database.AppDatabase
import com.example.weatherapp.database.LocalDataSource
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.RemoteDataSource
import com.example.weatherapp.views.home.HomeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MySoundAlarmReceiver : BroadcastReceiver(){
    private lateinit var appRepo:AppRepository
    private lateinit var weatherData : WeatherResponse
    private var language : String? = "en"
    private lateinit var settingSharedPreferences: SettingsSharedPreferences
    override fun onReceive(context: Context?, intent: Intent) {
//        val stopIntent = Intent(context, MyAlarmService::class.java)
//        context?.stopService(stopIntent)
        settingSharedPreferences = SettingsSharedPreferences
        language = settingSharedPreferences.getLanguage(context!!)
        val id = intent.getIntExtra("id",0)
        val lat = intent.getDoubleExtra("lat",0.0)
        val long = intent.getDoubleExtra("long",0.0)

        appRepo = AppRepository.getInstance(RemoteDataSource(),LocalDataSource(AppDatabase.getInstance(context!!)))
        CoroutineScope(Dispatchers.IO).launch{
            if(intent.getStringExtra("type") == "alarm")
            {
                val serviceIntent = Intent(context, MyAlarmService::class.java)
                context.startService(serviceIntent)
            }
            val reId= appRepo.deleteAlertById(id)
            Log.i("TAG", "onReceive: $reId")
            getCurrentWeather(lat,long)
            val current = weatherData.current
            showNotification(context, "Weather alert", "The weather in ${weatherData.timezone} is ${current.weather[0].main}\ntemp is ${current.temp}°C")
            Log.i("TAG", "onReceive: heeeeeeeee ${intent.extras}")
        }
    }
    private fun showNotification(context: Context, title: String, content: String) {
        val intent = Intent(context, MainActivity2::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("Alert", "Alert")
        }
        val dismissIntent = Intent(context, DismissReceiver::class.java).apply {
            action = "your_package_name.DISMISS_ACTION"
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val dismissPendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.cloud_2489384) // Update with your correct icon resource
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.baseline_done_24,"Dismiss",dismissPendingIntent)
            .setOngoing(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alerts"
            val descriptionText = "Allows the application to send you alert notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())

    }
    private suspend fun getCurrentWeather(lat:Double, long:Double)
    {
        Log.i("TAG", "getCurrentWeather: $language")
        appRepo.getAllForecastData(lat,long, HomeFragment.API_KEY, lang = language!! ).collect(){
            weatherData=it
        }
    }
    private suspend fun deleteWithId(id:Int)
    {
        appRepo.deleteAlertById(id)
    }
}