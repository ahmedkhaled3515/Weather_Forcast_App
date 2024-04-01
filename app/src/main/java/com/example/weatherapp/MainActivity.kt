package com.example.weatherapp

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.ViewModel.SharedSettingsViewModel
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView:NavigationView
    private val sharedSettingsViewModel : SharedSettingsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding
//        binding = ActivityMainBinding.inflate(layoutInflater)
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
//        setContentView(binding.root)
//
//
//        // Initialize NavController, DrawerLayout, and NavigationView
//        navController = findNavController(R.id.fragmentContainerView)
//        drawerLayout = binding.drawerLayout
//        navView = binding.navigationView
//
//        // Set up the navigation with NavController and DrawerLayout
//        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
////        showNotification(this,"ahmed","sonss")
//        lifecycleScope.launch {
//            sharedSettingsViewModel.language.collect(){
//                changeLanguage(it)
//            }
//        }
//
//    }
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
//
//
//    override fun onSupportNavigateUp(): Boolean {
//        return  navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
    }
}