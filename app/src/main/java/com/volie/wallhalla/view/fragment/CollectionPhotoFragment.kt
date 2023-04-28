package com.volie.wallhalla.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.volie.wallhalla.databinding.FragmentCollectionPhotoBinding
import com.volie.wallhalla.util.Status
import com.volie.wallhalla.view.adapter.CollectionFeedPhotoAdapter
import com.volie.wallhalla.view.viewmodel.CollectionPhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionPhotoFragment : Fragment() {
    private var _mBinding: FragmentCollectionPhotoBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: CollectionPhotoViewModel by viewModels()
    private val mAdapter: CollectionFeedPhotoAdapter by lazy {
        CollectionFeedPhotoAdapter(
            onItemClick = {

            },
            onFavClick = { photo, position ->

            }
        )
    }

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

        val collectionId = arguments?.getString("collectionId")

        setupRecyclerView()
        observeLiveData()
        mViewModel.getCollectionPhotos(collectionId!!, 1)
    }

    private fun setupRecyclerView() {
        with(mBinding.rvCollectionPhoto) {
            adapter = mAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun observeLiveData() {
        mViewModel.photos.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { data ->
                        mAdapter.submitList(data.media)
                    }
                }

                Status.ERROR -> {
                    it.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }

                Status.LOADING -> {
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}