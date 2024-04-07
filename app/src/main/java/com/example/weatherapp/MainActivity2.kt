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
import com.bumptech.glide.Glide
import com.example.weatherapp.ViewModel.SharedSettingsViewModel
import com.example.weatherapp.databinding.ActivityMain2Binding
import com.example.weatherapp.databinding.NavHeaderMainBinding
import java.text.NumberFormat
import java.util.Locale

class MainActivity2 : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMain2Binding
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
        updateNavigationHeader()
        val settingsSharedPreferences = SettingsSharedPreferences
        settingsSharedPreferences.saveLanguage(this,"en")
        settingsSharedPreferences.saveUnits(this,"metric")
//        changeLanguage(settingsSharedPreferences.getLanguage(this)!!)
    }




    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun updateActionBarTitle(title: String) {
        supportActionBar?.title = title
    }
    fun updateNavigationHeader() {
        val locale = Locale.getDefault()
        val numberFormat = NumberFormat.getInstance(locale)
        val forecast = WeatherSharedPreferences.getWeatherResponse(this)!!.current
        val url = "https://openweathermap.org/img/wn/${forecast.weather[0].icon}@2x.png"
        val temp = "${numberFormat.format(forecast.temp)}°c"

        val desc =forecast.weather[0].description
        val navView = binding.navView
        val headerView = NavHeaderMainBinding.bind(navView.getHeaderView(0))
        headerView.description.text = if(locale.language == "en")
        {
            translateToEnglish(desc)
        }
        else
        {
            translateToArabic(desc)
        }
        headerView.temp.text=temp
        Glide.with(this).load(url).into(headerView.navImage)
    }
    private fun translateToEnglish(word :String) : String{
        var translated = word
        when(word)
        {
            "سماء صافية" -> translated = "clear sky"
            "قليل الغيوم" -> translated = "few clouds"
            "غيوم متفرقة" -> translated = "scattered clouds"
            "غيوم متقطعة"-> translated ="broken clouds"
            "امطار غزيرة"-> translated ="shower rain"
            "امطار" -> translated = "rain"
            "عاصفة رعدية"-> translated = "thunderstorm"
            "مثلج"->translated = "snow"
            "شَبُّورَة"->translated = "mist"
            "رذاذ"->translated ="Drizzle"
            "امطار مثلجة"->translated =  "freezing rain"
            "غيوم كثيفة"->translated = "overcast clouds"
        }
        return translated
    }
    private fun translateToArabic(word :String) : String{
        var translated = word
        when(word)
        {
            "clear sky" -> translated = "سماء صافية"
            "few clouds"-> translated = "قليل الغيوم"
            "scattered clouds"-> translated = "غيوم متفرقة"
            "broken clouds"-> translated = "غيوم متقطعة"
            "shower rain"-> translated = "امطار غزيرة"
            "rain"-> translated = "امطار"
            "thunderstorm"-> translated = "عاصفة رعدية"
            "snow"->translated = "مثلج"
            "mist"->translated = "شَبُّورَة"
            "Drizzle"->translated = "رذاذ"
            "freezing rain"->translated = "امطار مثلجة"
            "overcast clouds"->translated = "غيوم كثيفة"


        }
        return translated
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