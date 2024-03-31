package com.example.weatherapp.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.database.AppDatabase
import com.example.weatherapp.database.LocalDataSource
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.RemoteDataSource
import com.example.weatherapp.sevices.MyAlarmService
import com.example.weatherapp.views.home.HomeFragment.Companion.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


//class AlertWorker(val context: Context, val workerParameters: WorkerParameters) : CoroutineWorker(context,workerParameters) {
//    var count = 0
//    val appRepo = AppRepository.getInstance(RemoteDataSource(),LocalDataSource(AppDatabase.getInstance(context)))
//    companion object{
//        final const val CHANNEL_ID="1"
//        final const val NOTIFICATION_ID =1
//    }
//    private lateinit var weatherData:WeatherResponse
//    override suspend fun doWork(): Result {
//        val data = inputData
//        val long = data.getDouble("long",0.0)
//        val lat = data.getDouble("lat",0.0)
//        val id = data.getInt("id",0)
//        Log.i("TAG", "doWork: $id")
//        return try {
//            withContext(Dispatchers.IO) {
//                getCurrentWeather(lat, long)
//            }
//            Log.i("TAG", "doWork: Weather data fetched successfully")
//            val current = weatherData.current
//            showNotification(context, "Weather alert", "The weather in ${weatherData.timezone} is ${current.weather[0].main}\ntemp is ${current.temp}°C")
//            deleteWithId(id)
//            Result.success()
//        } catch (e: Exception) {
//            Log.e("TAG", "Error fetching weather data", e)
//            Result.failure()
//        }
//    }
//    private fun showNotification(context: Context, title: String, content: String) {
//        val intent = Intent(context, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            putExtra("Alert", "Alert")
//        }
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//
//        val builder = NotificationCompat.Builder(context, AlertWorker.CHANNEL_ID)
//            .setSmallIcon(R.drawable.cloud_2489384) // Update with your correct icon resource
//            .setContentTitle(title)
//            .setContentText(content)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "Alerts"
//            val descriptionText = "Allows the application to send you alert notification"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(AlertWorker.CHANNEL_ID, name, importance).apply {
//                description = descriptionText
//            }
//            val notificationManager: NotificationManager =
//                context.getSystemService(NotificationManager::class.java) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        val notificationManager: NotificationManager =
//            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(NOTIFICATION_ID, builder.build())
//
//    }
//    private suspend fun getCurrentWeather(lat:Double, long:Double)
//    {
//        appRepo.getAllForecastData(lat,long,API_KEY).collect(){
//            weatherData=it
//        }
//    }
//    private suspend fun deleteWithId(id:Int)
//    {
//        appRepo.deleteAlertById(id)
//    }
//    private fun showContinuousRingtoneNotification(context: Context) {
//        val notificationLayout = RemoteViews(context.packageName, R.layout.alarm_notification_layout)
//        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setSmallIcon(R.drawable.cloud_2489384)
//            .setCustomContentView(notificationLayout)
//            .build()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "Alerts"
//            val descriptionText = "Allows the application to send you alert notification"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(AlertWorker.CHANNEL_ID, name, importance).apply {
//                description = descriptionText
//            }
//            val notificationManager: NotificationManager =
//                context.getSystemService(NotificationManager::class.java) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        val notificationManager: NotificationManager =
//            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(NOTIFICATION_ID, notification)
//
//        // Start the RingtoneService to play the ringtone
//        val ringtoneIntent = Intent(context, MyAlarmService::class.java)
//        ContextCompat.startForegroundService(context, ringtoneIntent)
//    }
//}