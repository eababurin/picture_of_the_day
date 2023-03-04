package ru.eababurin.pictureoftheday.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.eababurin.pictureoftheday.R
import ru.eababurin.pictureoftheday.databinding.FragmentMainBinding
import ru.eababurin.pictureoftheday.view.navigation.EarthFragment
import ru.eababurin.pictureoftheday.view.navigation.MarsFragment
import ru.eababurin.pictureoftheday.view.navigation.SettingsFragment
import ru.eababurin.pictureoftheday.view.navigation.SystemFragment

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNavigationView.apply {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.bottom_view_earth -> {
                        navigationTo(EarthFragment())
                        true
                    }
                    R.id.bottom_view_mars -> {
                        navigationTo(MarsFragment())
                        true
                    }
                    R.id.bottom_view_system -> {
                        navigationTo(SystemFragment())
                        true
                    }
                    R.id.bottom_view_settings -> {
                        navigationTo(SettingsFragment())
                        true
                    }
                    else -> false
                }
            }
            selectedItemId = R.id.bottom_view_earth
        }
    }

    private fun navigationTo(fragment: Fragment) {
        parentFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}