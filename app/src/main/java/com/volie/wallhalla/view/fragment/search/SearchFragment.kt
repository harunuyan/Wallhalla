package com.volie.wallhalla.view.fragment.search

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.volie.wallhalla.databinding.FragmentSearchBinding
import com.volie.wallhalla.util.PaginationScrollListener
import com.volie.wallhalla.util.Status
import com.volie.wallhalla.view.fragment.home.FeedAdapter
import com.volie.wallhalla.view.fragment.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _mBinding: FragmentSearchBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: SearchViewModel by viewModels()
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
    private var query: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBackPressed()
        setupRecyclerView()

        searchForImage()
        observeLiveData()
    }

    private fun searchForImage() {
        var job: Job? = null

        mBinding.etSearch.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(1000L)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        query = it.toString()
                        mViewModel.getSearchResult(it.toString(), currentPage)
                    }
                }
            }
        }
    }

    private fun observeLiveData() {
        mViewModel.searchData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    mBinding.progressBar.visibility = View.GONE
                    isLoading = false
                    it.data?.let { photos ->
                        mAdapter.submitList(photos.photos)
                    }
                }

                Status.LOADING -> {
                    mBinding.progressBar.visibility = View.VISIBLE
                }

                Status.ERROR -> {
                    mBinding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val mLayoutManager = GridLayoutManager(requireContext(), 2)
        with(mBinding.rvSearch) {
            adapter = mAdapter
            layoutManager = mLayoutManager
            addOnScrollListener(object : PaginationScrollListener(mLayoutManager) {
                override fun loadMoreItems() {
                    isLoading = true
                    currentPage++

                    Handler(android.os.Looper.myLooper()!!).postDelayed({
                        mViewModel.getSearchResult(query, currentPage)
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

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (currentPage == pageStart) {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    } else {
                        currentPage--
                        mViewModel.getSearchResult(query, currentPage)
                    }
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }
}
