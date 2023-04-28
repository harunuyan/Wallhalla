package com.volie.wallhalla.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.volie.wallhalla.databinding.FragmentFeedBinding
import com.volie.wallhalla.util.PaginationScrollListener
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
        FeedAdapter(
            onItemClick = {
                val action = HomeFragmentDirections.actionHomeFragmentToPhotoDetailsFragment(it)
                findNavController().navigate(action)
            },
            onFavClick = { photo, position ->
                if (!photo.isLiked) {
                    photo.isLiked = true

                    mViewModel.savePhoto(photo)
                } else {
                    photo.isLiked = false
                    mViewModel.deletePhoto(photo)
                }
                mAdapter.notifyItemChanged(position)
            }
        )
    }
    private val pageStart: Int = 1
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var totalPages: Int = 1
    private var currentPage: Int = pageStart

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

        onBackPressed()
        setupRecyclerView()
        setupPullToRefresh()
        observeLiveData()
        mViewModel.getWallpapers(currentPage)
    }

    private fun observeLiveData() {
        mViewModel.wallpapers.observe(viewLifecycleOwner) {
            with(mBinding) {
                when (it.status) {
                    Status.SUCCESS -> {
                        pbFeed.visibility = View.GONE
                        rvFeed.visibility = View.VISIBLE
                        tvNoInternet.visibility = View.GONE
                        isLoading = false
                        it.data?.let { data ->
                            mAdapter.submitList(data.media)
                        }
                    }

                    Status.ERROR -> {
                        pbFeed.visibility = View.GONE
                        rvFeed.visibility = View.GONE
                        tvNoInternet.visibility = View.VISIBLE
                        it.message?.let { message ->
                            Log.e("FeedFragment", "An error occured: $message")
                        }
                    }

                    Status.LOADING -> {
                        pbFeed.visibility = View.VISIBLE
                        rvFeed.visibility = View.GONE
                        tvNoInternet.visibility = View.GONE
                        Log.d("FeedFragment", "Loading...")
                    }
                }
            }

        }
    }

    private fun setupRecyclerView() {
        val mLayoutManager = GridLayoutManager(requireContext(), 2)
        with(mBinding.rvFeed) {
            adapter = mAdapter
            layoutManager = mLayoutManager
            addOnScrollListener(object : PaginationScrollListener(mLayoutManager) {
                override fun loadMoreItems() {
                    isLoading = true
                    currentPage++

                    Handler(Looper.myLooper()!!).postDelayed({
                        mViewModel.getWallpapers(currentPage)
                    }, 1000)
                }

                override fun getTotalPageCount(): Int {
                    return totalPages
                }

                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }

            })
        }
    }

    private fun setupPullToRefresh() {
        mBinding.srlFeed.setOnRefreshListener {
            mViewModel.getWallpapers(pageStart)
            mBinding.srlFeed.isRefreshing = false
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (currentPage == pageStart) {
                        findNavController().navigateUp()
                    } else {
                        currentPage--
                        mViewModel.getWallpapers(currentPage)
                    }
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}