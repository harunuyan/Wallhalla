package com.volie.wallhalla.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.volie.wallhalla.databinding.FragmentCollectionBinding
import com.volie.wallhalla.view.adapter.CollectionAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionFragment : Fragment() {
    private var _mBinding: FragmentCollectionBinding? = null
    private val mBinding get() = _mBinding!!
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

    private fun setupRecyclerView() {
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