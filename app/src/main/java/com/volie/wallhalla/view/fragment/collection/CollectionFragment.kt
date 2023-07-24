package com.volie.wallhalla.view.fragment.collection

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
import com.volie.wallhalla.databinding.FragmentCollectionBinding
import com.volie.wallhalla.util.PaginationScrollListener
import com.volie.wallhalla.util.Status
import com.volie.wallhalla.view.adapter.CollectionAdapter
import com.volie.wallhalla.view.viewmodel.collection_vm.CollectionViewModel
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
                    CollectionFragmentDirections.actionCollectionFragmentToCollectionFeedFragment(it)
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

        onBackPressed()
        setupRecyclerView()
        observeLiveData()
        mViewModel.getFeaturedCollections(currentPage)
    }

    private fun observeLiveData() {
        mViewModel.collections.observe(viewLifecycleOwner) { resource ->
            with(mBinding) {
                when (resource.status) {
                    Status.SUCCESS -> {
                        pbCollection.visibility = View.GONE
                        rvCollection.visibility = View.VISIBLE
                        isLoading = false
                        resource.data?.let {
                            mAdapter.submitList(it.collections)
                        }
                    }

                    Status.ERROR -> {
                        pbCollection.visibility = View.VISIBLE
                        rvCollection.visibility = View.GONE
                    }

                    Status.LOADING -> {
                        pbCollection.visibility = View.VISIBLE
                        rvCollection.visibility = View.GONE
                    }
                }
            }

        }
    }

    private fun setupRecyclerView() {
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

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (currentPage == pageStart) {
                        findNavController().navigateUp()
                    } else {
                        currentPage--
                        mViewModel.getFeaturedCollections(currentPage)
                    }
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}