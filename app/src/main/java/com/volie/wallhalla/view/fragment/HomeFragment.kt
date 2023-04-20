package com.volie.wallhalla.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.volie.wallhalla.databinding.FragmentHomeBinding
import com.volie.wallhalla.view.adapter.HomeViewPagerAdapter

class HomeFragment : Fragment() {
    private var _mBinding: FragmentHomeBinding? = null
    private val mBinding get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        setupViewPager()
        return mBinding.root
    }

    private fun setupViewPager() {
        val adapter = HomeViewPagerAdapter(requireActivity())
        mBinding.viewPagerHome.adapter = adapter
        TabLayoutMediator(mBinding.tabLayoutHome, mBinding.viewPagerHome) { tab, position ->
            when (position) {
                0 -> tab.text = "Feed"
                1 -> tab.text = "Search"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }
}