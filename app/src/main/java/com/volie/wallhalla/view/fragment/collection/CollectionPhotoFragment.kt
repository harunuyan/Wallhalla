package com.volie.wallhalla.view.fragment.collection

import android.os.Bundle
import android.os.Handler
import android.os.Looper.myLooper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.volie.wallhalla.databinding.FragmentCollectionPhotoBinding
import com.volie.wallhalla.util.PaginationScrollListener
import com.volie.wallhalla.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionPhotoFragment : Fragment() {
    private var _mBinding: FragmentCollectionPhotoBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: CollectionPhotoViewModel by viewModels()
    private val mAdapter: CollectionFeedPhotoAdapter by lazy {
        CollectionFeedPhotoAdapter(
            onItemClick = {
                val action =
                    CollectionFeedFragmentDirections.actionCollectionFeedFragmentToPhotoDetailsFragment(
                        it
                    )
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
        _mBinding = FragmentCollectionPhotoBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectionId = arguments?.getString("collectionId")!!
        onBackPressed()
        setupRecyclerView()
        observeLiveData()
        mViewModel.getCollectionPhotos(collectionId, 1)
    }

    private fun setupRecyclerView() {
        val mLayoutManager = GridLayoutManager(requireContext(), 2)
        with(mBinding.rvCollectionPhoto) {
            adapter = mAdapter
            layoutManager = mLayoutManager
            addOnScrollListener(object : PaginationScrollListener(mLayoutManager) {
                override fun loadMoreItems() {
                    isLoading = true
                    currentPage++

                    Handler(myLooper()!!).postDelayed({
                        mViewModel.getCollectionPhotos(collectionId, currentPage)
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
        mViewModel.photos.observe(viewLifecycleOwner) {
            with(mBinding) {
                when (it.status) {
                    Status.SUCCESS -> {
                        pbCollectionPhoto.visibility = View.GONE
                        rvCollectionPhoto.visibility = View.VISIBLE
                        isLoading = false
                        it.data?.let { data ->
                            mAdapter.submitList(data.media)
                        }
                    }

                    Status.ERROR -> {
                        pbCollectionPhoto.visibility = View.GONE
                        rvCollectionPhoto.visibility = View.VISIBLE
                        it.message?.let { message ->
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    Status.LOADING -> {
                        pbCollectionPhoto.visibility = View.VISIBLE
                        rvCollectionPhoto.visibility = View.GONE
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
                        mViewModel.getCollectionPhotos(collectionId, currentPage)
                    }
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}