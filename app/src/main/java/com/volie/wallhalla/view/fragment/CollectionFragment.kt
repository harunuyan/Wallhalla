package com.volie.wallhalla.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.volie.wallhalla.databinding.FragmentCollectionBinding
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
        mViewModel.getFeaturedCollections()
    }

    private fun observeLiveData() {
        mViewModel.collections.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let {
                        mAdapter.submitList(it.collections)
                    }
                }

                Status.ERROR -> {
                }

                Status.LOADING -> {
                }
            }
        }
    }

    fun setupRecyclerView() {
        with(mBinding) {
            rvCollection.adapter = mAdapter
            rvCollection.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}