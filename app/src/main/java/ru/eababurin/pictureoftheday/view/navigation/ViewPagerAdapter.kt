package ru.eababurin.pictureoftheday.view.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.eababurin.pictureoftheday.utils.EARTH
import ru.eababurin.pictureoftheday.utils.MARS
import ru.eababurin.pictureoftheday.utils.SOLAR_SYSTEM

class ViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val fragments = arrayOf(EarthFragment(), MarsFragment(), SystemFragment())

    override fun getCount(): Int = fragments.size
    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> EARTH
        1 -> MARS
        else -> SOLAR_SYSTEM
    }
}