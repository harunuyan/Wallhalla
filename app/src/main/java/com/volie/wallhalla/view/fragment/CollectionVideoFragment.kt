package com.volie.wallhalla.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.volie.wallhalla.databinding.FragmentCollectionVideoBinding
import com.volie.wallhalla.util.PaginationScrollListener
import com.volie.wallhalla.util.Status
import com.volie.wallhalla.view.adapter.FeedAdapter
import com.volie.wallhalla.view.viewmodel.CollectionVideoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionVideoFragment : Fragment() {
    private var _mBinding: FragmentCollectionVideoBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: CollectionVideoViewModel by viewModels()
    private val mAdapter: FeedAdapter by lazy {
        FeedAdapter(
            onItemClick = {
                val action =
                    CollectionFeedFragmentDirections.actionCollectionFeedFragmentToVideoPlayWebFragment(
                        it
                    )
                findNavController().navigate(action)
            },
            onFavClick = { video, position ->
                if (!video.isLiked) {
                    mViewModel.saveVideo(video)
                    video.isLiked = true
                } else {
                    mViewModel.deleteVideo(video)
                    video.isLiked = false
                }
                mAdapter.notifyItemChanged(position)
            }
        )
    }
    private var collectionId = ""
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
        _mBinding = FragmentCollectionVideoBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBackPressed()
        collectionId = arguments?.getString("collectionId")!!
        setupRecyclerView()
        observeLiveData()
        mViewModel.getVideosPictures(collectionId, currentPage)
    }

    private fun setupRecyclerView() {
        val mLayoutManager = GridLayoutManager(requireContext(), 1)
        with(mBinding.recyclerViewCollectionVideo) {
            adapter = mAdapter
            layoutManager = mLayoutManager
            addOnScrollListener(object : PaginationScrollListener(mLayoutManager) {
                override fun loadMoreItems() {
                    isLoading = true
                    currentPage++

                    Handler(Looper.myLooper()!!).postDelayed({
                        mViewModel.getVideosPictures(collectionId, currentPage)
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

    private fun observeLiveData() {
        mViewModel.videosPictures.observe(viewLifecycleOwner) {
            with(mBinding) {
                when (it.status) {
                    Status.SUCCESS -> {
                        recyclerViewCollectionVideo.visibility = View.VISIBLE
                        pbCollectionVideo.visibility = View.GONE
                        isLoading = false
                        it.data?.let { data ->
                            mAdapter.submitList(data.media)
                        }
                    }

                    Status.ERROR -> {
                        recyclerViewCollectionVideo.visibility = View.GONE
                        pbCollectionVideo.visibility = View.VISIBLE
                    }

                    Status.LOADING -> {
                        pbCollectionVideo.visibility = View.VISIBLE
                        recyclerViewCollectionVideo.visibility = View.GONE
                    }
                }
            }

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
                        mViewModel.getVideosPictures(collectionId, currentPage)
                    }
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}