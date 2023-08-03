package com.volie.wallhalla.view.fragment.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.volie.wallhalla.databinding.FragmentFavoriteBinding
import com.volie.wallhalla.view.fragment.home.FeedAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var _mBinding: FragmentFavoriteBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: FavoriteViewModel by viewModels()
    private val mAdapter: FeedAdapter by lazy {
        FeedAdapter(
            onItemClick = {
                if (it.type == "Video") {
                    val action =
                        FavoriteFragmentDirections.actionFavoriteFragmentToVideoPlayWebFragment(it)
                    findNavController().navigate(action)
                } else {
                    val action =
                        FavoriteFragmentDirections.actionFavoriteFragmentToPhotoDetailsFragment(it)
                    findNavController().navigate(action)
                }

            },
            onFavClick = { photo, _ ->
                mViewModel.deletePhoto(photo)
                mViewModel.getSavedPhotos()
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.ivEmptyStorage.setOnClickListener {
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToCollectionFragment()
            findNavController().navigate(action)
        }

        setupRecyclerView()
        mViewModel.getSavedPhotos()
        observeLiveData()
    }

    private fun observeLiveData() {
        mViewModel.savedPhotos.observe(viewLifecycleOwner) {

            if (it.isNullOrEmpty()) {
                mBinding.rvFavorite.visibility = View.GONE
                mBinding.ivEmptyStorage.visibility = View.VISIBLE
                mBinding.ivEmptyStorage.playAnimation()
            } else {
                mBinding.rvFavorite.visibility = View.VISIBLE
                mBinding.ivEmptyStorage.visibility = View.GONE
                mBinding.ivEmptyStorage.cancelAnimation()
                mAdapter.submitList(it)
            }
        }
    }

    private fun setupRecyclerView() {
        with(mBinding.rvFavorite) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}