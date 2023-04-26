package com.volie.wallhalla.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.volie.wallhalla.databinding.FragmentCollectionBinding
import com.volie.wallhalla.util.PaginationScrollListener
import com.volie.wallhalla.util.Status
import com.volie.wallhalla.view.adapter.CollectionAdapter
import com.volie.wallhalla.view.viewmodel.CollectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionFragment : Fragment() {
    private var _mBinding: FragmentCollectionBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: CollectionViewModel by viewModels()
    private val mAdapter by lazy {
        CollectionAdapter(
            onItemClick = {
                val action =
                    CollectionFragmentDirections.actionCollectionFragmentToCollectionFeedFragment()
                findNavController().navigate(action)
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
        _mBinding = FragmentCollectionBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeLiveData()
        mViewModel.getFeaturedCollections(currentPage)
    }

    private fun observeLiveData() {
        mViewModel.collections.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    mBinding.pbCollection.visibility = View.GONE
                    isLoading = false
                    resource.data?.let {
                        mAdapter.submitList(it.collections)
                    }
                }

                Status.ERROR -> {
                }

                Status.LOADING -> {
                    mBinding.pbCollection.visibility = View.VISIBLE
                }
            }
        }
    }

    fun setupRecyclerView() {
        val mLayoutManager = GridLayoutManager(requireContext(), 1)
        with(mBinding.rvCollection) {
            adapter = mAdapter
            layoutManager = mLayoutManager
            addOnScrollListener(object : PaginationScrollListener(mLayoutManager) {
                override fun loadMoreItems() {
                    isLoading = true
                    currentPage++

                    Handler(Looper.myLooper()!!).postDelayed({
                        mViewModel.getFeaturedCollections(currentPage)
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

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}