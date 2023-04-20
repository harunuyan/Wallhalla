package com.volie.wallhalla.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.volie.wallhalla.view.fragment.FeedFragment
import com.volie.wallhalla.view.fragment.SearchFragment

class HomeViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FeedFragment()
            1 -> SearchFragment()
            else -> FeedFragment()
        }
    }
}