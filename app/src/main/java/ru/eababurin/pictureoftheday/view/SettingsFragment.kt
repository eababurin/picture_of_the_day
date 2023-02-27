package ru.eababurin.pictureoftheday.view

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomappbar.BottomAppBar
import ru.eababurin.pictureoftheday.R
import ru.eababurin.pictureoftheday.databinding.FragmentSettingsBinding
import ru.eababurin.pictureoftheday.utils.THEME

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomAppBar: BottomAppBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomAppBar = binding.bottomAppBarOnSettings.apply {
            setNavigationOnClickListener {
                val bottomNavigationDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavigationDrawerFragment.show(
                    parentFragmentManager,
                    bottomNavigationDrawerFragment.tag
                )
            }
            menu.findItem(R.id.app_bar_settings).isEnabled = false
        }

        val sharedPreferences: SharedPreferences = requireActivity().getPreferences(MODE_PRIVATE)
        val spEditor = sharedPreferences.edit()

        binding.chipThemeUsual.setOnClickListener {
            spEditor.apply {
                putInt(THEME, R.style.Theme_PictureOfTheDay)
                apply()
                requireActivity().recreate()
            }
        }

        binding.chipThemeMars.setOnClickListener {
            spEditor.apply {
                putInt(THEME, R.style.Theme_PictureOfTheDay_Mars)
                apply()
                requireActivity().recreate()
            }
        }

        binding.chipThemeSpace.setOnClickListener {
            spEditor.apply {
                putInt(THEME, R.style.Theme_PictureOfTheDay_Space)
                apply()
                requireActivity().recreate()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}