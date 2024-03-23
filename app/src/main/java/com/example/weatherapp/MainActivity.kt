package com.example.weatherapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.views.AlertFragment
import com.example.weatherapp.views.MapsFragment
import com.example.weatherapp.workers.AlertWorker
import com.google.android.material.navigation.NavigationView
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView:NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
//        val extras = intent.extras
//        Log.i("TAG", "onCreate intent: ${intent.extras}")
//        if(extras != null && extras.containsKey("Alert"))
//        {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainerView,AlertFragment())
//                .commit()
//        }else
//        {
//        }
        setContentView(binding.root)


        // Initialize NavController, DrawerLayout, and NavigationView
        navController = findNavController(R.id.fragmentContainerView)
        drawerLayout = binding.drawerLayout
        navView = binding.navigationView

        // Set up the navigation with NavController and DrawerLayout
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        showNotification(this,"ahmed","sonss")

    }


    override fun onSupportNavigateUp(): Boolean {
        return  navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}