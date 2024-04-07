package com.example.weatherapp.views.settings

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.MainActivity2
import com.example.weatherapp.R
import com.example.weatherapp.SettingsSharedPreferences
import com.example.weatherapp.ViewModel.SharedSettingsViewModel
import com.example.weatherapp.databinding.FragmentSettingsBinding
import java.util.Locale

class SettingsFragment : Fragment() {
    var lang= "en"
    var unit = "metric"
    private val sharedSettingsViewModel : SharedSettingsViewModel by activityViewModels()
    private lateinit var settingsSharedPreferences: SettingsSharedPreferences
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsSharedPreferences = SettingsSharedPreferences
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
        languageSwitch()
        temperatureSwitch()
        locationSwitch(view)
        binding.recreateButton.setOnClickListener {
            Log.i("TAG", "onViewCreated: $lang")
            sharedSettingsViewModel.changeLanguage(lang)
            sharedSettingsViewModel.changeUnit(unit)
            changeLanguage(lang)
            activity?.recreate()
        }
    }
    private fun initializeUI()
    {
        val initUnit = settingsSharedPreferences.getUnits(requireContext())
        val initLang = settingsSharedPreferences.getLanguage(requireContext())
        val initLocation = settingsSharedPreferences.getLocations(requireContext())
        if(initLocation == "gps")
        {
            binding.gpsRadioButton.isChecked=true
        }
        else
        {
            binding.mapRadioButton.isChecked=true
        }
        if(initLang == "en")
        {
            binding.englishRadioButton.isChecked= true
        }
        else
        {
            binding.arabicRadioButton.isChecked = true
        }
        if(initUnit == "metric")
        {
            binding.celsiusRadioButton.isChecked = true
        }
        else if ( initUnit == "standard")
        {
            binding.kelvinRadioButton.isChecked=true
        }
        else{
            binding.fahrenhietRadioButton.isChecked=true
        }
    }
    private fun locationSwitch(view : View)
    {
        binding.gpsRadioButton.isChecked=true
        binding.locationRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                binding.gpsRadioButton.id->{
                    settingsSharedPreferences.saveLocation(requireContext(),"gps")
                    sharedSettingsViewModel.changeLocation("gps")
                    val bundle = Bundle()
                    bundle.putString("type","gps")
                    Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_homeFragment,bundle)
                }
                binding.mapRadioButton.id->{
                    settingsSharedPreferences.saveLocation(requireContext(),"map")
                    sharedSettingsViewModel.changeLocation("map")
                    val bundle = Bundle()
                    bundle.putString("sourceFragment","settings")
                    Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_mapsFragment,bundle)
                }
            }
        }
    }

    private fun languageSwitch()
    {

        binding.languageRadioGroup.setOnCheckedChangeListener(){ radioGroup: RadioGroup, id: Int ->
            when (id) {
                binding.arabicRadioButton.id -> {
//                    Toast.makeText(requireContext(), "Arabic", Toast.LENGTH_SHORT).show()
                    lang = "ar"
                    settingsSharedPreferences.saveLanguage(requireContext(),"ar")
                }
                binding.englishRadioButton.id -> {
//                    Toast.makeText(requireContext(), "English", Toast.LENGTH_SHORT).show()
                    lang ="en"
                    settingsSharedPreferences.saveLanguage(requireContext(),"en")
                }
            }
        }
        Log.i("TAG", "languageSwitch: $lang")
    }
    private fun temperatureSwitch(){
        binding.temperatureRadioGroup.setOnCheckedChangeListener(){ radioGroup: RadioGroup, id: Int ->
            when(id){
                binding.celsiusRadioButton.id -> {

//                    Toast.makeText(requireContext(), "Celsius", Toast.LENGTH_SHORT).show()
                    unit = "metric"
                    settingsSharedPreferences.saveUnits(requireContext(),"metric")
                }
                binding.kelvinRadioButton.id->{
//                    Toast.makeText(requireContext(), "Kelvin", Toast.LENGTH_SHORT).show()
                    unit = "standard"
                    settingsSharedPreferences.saveUnits(requireContext(),"standard")

                }
                binding.fahrenhietRadioButton.id->{
//                    Toast.makeText(requireContext(), "Fahrenheit", Toast.LENGTH_SHORT).show()
                    unit = "imperial"
                    settingsSharedPreferences.saveUnits(requireContext(),"imperial")

                }
            }
        }
    }
    private fun changeLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)

        // Recreate the activity to apply the language change
//        requireActivity().recreate()
    }

}