package ru.eababurin.pictureoftheday.view.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.eababurin.pictureoftheday.R
import ru.eababurin.pictureoftheday.databinding.FragmentEarthBinding
import ru.eababurin.pictureoftheday.view.adapters.ViewPager2Adapter
import kotlin.math.abs

class EarthFragment : Fragment() {

    private var _binding: FragmentEarthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEarthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.apply {
            adapter = ViewPager2Adapter(requireActivity())
            setPageTransformer(object : ViewPager2.PageTransformer {
                override fun transformPage(page: View, position: Float) {
                    page.apply {
                        val pageWidth = width
                        when {
                            position < -1 -> {
                                alpha = 0f
                            }
                            position <= 0 -> {
                                alpha = 1f
                                translationX = 0f
                                translationZ = 0f
                                scaleX = 1f
                                scaleY = 1f
                            }
                            position <= 1 -> {
                                alpha = 1 - position
                                translationZ = -1f
                                translationX = pageWidth * -position

                                val scaleFactor = (0.75f + (1 - 0.75f) * (1 - abs(position)))
                                scaleX = scaleFactor
                                scaleY = scaleFactor
                            }
                            else -> {
                                alpha = 0f
                            }
                        }
                    }
                }

            })
        }

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager,
            object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    tab.text = when (position) {
                        0 -> resources.getString(R.string.today)
                        1 -> resources.getString(R.string.yesterday)
                        else -> resources.getString(R.string.day_before_yesterday)
                    }
                }
            }).attach()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}