package com.volie.wallhalla.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.volie.wallhalla.R
import com.volie.wallhalla.databinding.FragmentHomeBinding
import com.volie.wallhalla.view.adapter.BaseViewPagerAdapter
import com.volie.wallhalla.view.fragment.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _mBinding: FragmentHomeBinding? = null
    private val mBinding get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
    }

    private fun setupViewPager() {
        val pages = listOf(
            FeedFragment(),
            SearchFragment()
        )
        val adapter = BaseViewPagerAdapter(requireActivity(), pages)
        mBinding.viewPagerHome.adapter = adapter
        TabLayoutMediator(mBinding.tabLayoutHome, mBinding.viewPagerHome) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.ic_wallpapaer)
                1 -> tab.setIcon(R.drawable.ic_search)
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }
}