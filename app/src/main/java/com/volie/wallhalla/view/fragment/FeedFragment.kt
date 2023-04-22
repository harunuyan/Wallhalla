package com.volie.wallhalla.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.volie.wallhalla.databinding.FragmentFeedBinding
import com.volie.wallhalla.util.Status
import com.volie.wallhalla.view.adapter.FeedAdapter
import com.volie.wallhalla.view.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : Fragment() {
    private var _mBinding: FragmentFeedBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: FeedViewModel by viewModels()
    private val mAdapter: FeedAdapter by lazy {
        FeedAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentFeedBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        mViewModel.getWallpapers()
        observeLiveData()
    }

    private fun observeLiveData() {
        mViewModel.wallpapers.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { data ->
                        mAdapter.submitList(data.photos)
                    }
                }

                Status.ERROR -> {
                    it.message?.let { message ->
                        Log.e("FeedFragment", "An error occured: $message")
                    }
                }

                Status.LOADING -> {
                    Log.d("FeedFragment", "Loading...")
                }
            }
        }
    }

    private fun setupRecyclerView() {
        mBinding.rvFeed.adapter = mAdapter
        mBinding.rvFeed.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}