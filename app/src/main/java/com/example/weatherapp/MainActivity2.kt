package com.example.weatherapp

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.ViewModel.SharedSettingsViewModel
import com.example.weatherapp.databinding.ActivityMain2Binding
import java.util.Locale

class MainActivity2 : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain2Binding
    private val sharedSettingsViewModel : SharedSettingsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.favoriteFragment, R.id.settingsFragment,R.id.alertFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val settingsSharedPreferences = SettingsSharedPreferences
        settingsSharedPreferences.saveLanguage(this,"en")
        settingsSharedPreferences.saveUnits(this,"metric")
//        changeLanguage(settingsSharedPreferences.getLanguage(this)!!)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity2, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun updateActionBarTitle(title: String) {
        supportActionBar?.title = title
    }
//    private fun changeLanguage(languageCode: String) {
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//
//        val config = Configuration(resources.configuration)
//        config.setLocale(locale)
//
//        resources.updateConfiguration(config, resources.displayMetrics)
//
//        // Recreate the activity to apply the language change
////        requireActivity().recreate()
//    }
}