package com.example.weatherapp.views.settings

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
import com.example.weatherapp.ViewModel.SharedSettingsViewModel
import com.example.weatherapp.databinding.FragmentSettingsBinding
import java.util.Locale

class SettingsFragment : Fragment() {
    var lang= "en"
    var unit = "metric"
    private val sharedSettingsViewModel : SharedSettingsViewModel by activityViewModels()
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        languageSwitch()
        temperatureSwitch()
        binding.recreateButton.setOnClickListener {
            Log.i("TAG", "onViewCreated: $lang")
            sharedSettingsViewModel.changeLanguage(lang)
            sharedSettingsViewModel.changeUnit(unit)
            changeLanguage(lang)
            activity?.recreate()
        }
    }
    private fun languageSwitch()
    {

        binding.languageRadioGroup.setOnCheckedChangeListener(){ radioGroup: RadioGroup, id: Int ->
            when (id) {
                binding.arabicRadioButton.id -> {
                    Toast.makeText(requireContext(), "Arabic", Toast.LENGTH_SHORT).show()
                    lang = "ar"
                }
                binding.englishRadioButton.id -> {
                    Toast.makeText(requireContext(), "English", Toast.LENGTH_SHORT).show()
                    lang ="en"
                }
            }
        }
        Log.i("TAG", "languageSwitch: $lang")
    }
    private fun temperatureSwitch(){
        binding.temperatureRadioGroup.setOnCheckedChangeListener(){ radioGroup: RadioGroup, id: Int ->
            when(id){
                binding.celsiusRadioButton.id -> {
                    Toast.makeText(requireContext(), "Celsius", Toast.LENGTH_SHORT).show()
                    unit = "metric"
                }
                binding.kelvinRadioButton.id->{
                    Toast.makeText(requireContext(), "Kelvin", Toast.LENGTH_SHORT).show()
                    unit = "standard"
                }
                binding.fahrenhietRadioButton.id->{
                    Toast.makeText(requireContext(), "Fahrenheit", Toast.LENGTH_SHORT).show()
                    unit = "imperial"
                }
            }
        }
    }
    private fun changeLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)

        // Recreate the activity to apply the language change
//        requireActivity().recreate()
    }

}