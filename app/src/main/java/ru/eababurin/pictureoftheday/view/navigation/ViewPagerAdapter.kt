package ru.eababurin.pictureoftheday.view.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val fragments = arrayOf(EarthFragment, MarsFragment, SystemFragment)

    override fun getCount(): Int = fragments.size
    override fun getItem(position: Int): Fragment = fragments[position]
}