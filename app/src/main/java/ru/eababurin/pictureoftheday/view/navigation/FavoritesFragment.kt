package ru.eababurin.pictureoftheday.view.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.eababurin.pictureoftheday.databinding.FragmentFavoritesBinding
import ru.eababurin.pictureoftheday.utils.EARTH
import ru.eababurin.pictureoftheday.utils.MARS
import ru.eababurin.pictureoftheday.utils.SOLAR_SYSTEM

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = ViewPager2Adapter(requireActivity())

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager,
            object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    tab.text = when (position) {
                        0 -> EARTH
                        1 -> MARS
                        else -> SOLAR_SYSTEM
                    }
                }
            }).attach()
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}