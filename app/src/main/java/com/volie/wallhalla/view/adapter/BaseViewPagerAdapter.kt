package com.volie.wallhalla.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BaseViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val pages: List<Fragment>
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment {
        return pages[position]

    }
}