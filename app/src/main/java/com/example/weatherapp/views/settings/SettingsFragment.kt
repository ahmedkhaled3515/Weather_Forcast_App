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
    }
    private fun languageSwitch()
    {
        binding.languageRadioGroup.setOnCheckedChangeListener(){ radioGroup: RadioGroup, id: Int ->
            when (id) {
                binding.arabicRadioButton.id -> {
                    Toast.makeText(requireContext(), "Arabic", Toast.LENGTH_SHORT).show()
                    // Assuming sharedSettingsViewModel is initialized and changeLanguage function exists
                    sharedSettingsViewModel.changeLanguage("ar")
                    changeLanguage("ar")
                    // Recreate the activity to apply language changes
//                    requireActivity().recreate()
                }
                binding.englishRadioButton.id -> {
                    Toast.makeText(requireContext(), "English", Toast.LENGTH_SHORT).show()
                    sharedSettingsViewModel.changeLanguage("en")
                    // Handle switching to English language if needed
                    // Optionally, you can call sharedSettingsViewModel.changeLanguage("en")
                }
            }
        }
    }
    private fun temperatureSwitch(){
        binding.temperatureRadioGroup.setOnCheckedChangeListener(){ radioGroup: RadioGroup, id: Int ->
            when(id){
                binding.celsiusRadioButton.id -> {
                    Toast.makeText(requireContext(), "Celsius", Toast.LENGTH_SHORT).show()
                }
                binding.kelvinRadioButton.id->{
                    Toast.makeText(requireContext(), "Kelvin", Toast.LENGTH_SHORT).show()
                }
                binding.fahrenhietRadioButton.id->{
                    Toast.makeText(requireContext(), "Fahrenheit", Toast.LENGTH_SHORT).show()
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