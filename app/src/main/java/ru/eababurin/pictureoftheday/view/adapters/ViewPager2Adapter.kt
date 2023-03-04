package ru.eababurin.pictureoftheday.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.eababurin.pictureoftheday.view.navigation.earth.PhotoBeforeYesterdayFragment
import ru.eababurin.pictureoftheday.view.navigation.earth.PhotoTodayFragment
import ru.eababurin.pictureoftheday.view.navigation.earth.PhotoYesterdayFragment

class ViewPager2Adapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val fragments = arrayOf(PhotoTodayFragment(), PhotoYesterdayFragment(), PhotoBeforeYesterdayFragment())

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}