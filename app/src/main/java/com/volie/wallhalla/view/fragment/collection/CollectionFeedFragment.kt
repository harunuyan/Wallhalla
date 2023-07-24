package com.volie.wallhalla.view.fragment.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.volie.wallhalla.R
import com.volie.wallhalla.databinding.FragmentCollectionFeedBinding
import com.volie.wallhalla.view.adapter.ViewPagerAdapter

class CollectionFeedFragment : Fragment() {
    private var _mBinding: FragmentCollectionFeedBinding? = null
    private val mBinding get() = _mBinding!!
    private val args: CollectionFeedFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentCollectionFeedBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()

        mBinding.tvCollectionHeader.text = args.info.title
    }

    private fun setupViewPager() {
        val pages = listOf(
            CollectionPhotoFragment().apply {
                arguments = bundleOf("collectionId" to args.info.id)
            },
            CollectionVideoFragment().apply {
                arguments = bundleOf("collectionId" to args.info.id)
            }
        )
        val adapter = ViewPagerAdapter(requireActivity(), pages)
        mBinding.viewPagerFeed.adapter = adapter
        TabLayoutMediator(mBinding.tabLayoutFeed, mBinding.viewPagerFeed) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.ic_collection_photo)
                1 -> tab.setIcon(R.drawable.ic_collection_video)
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }
}