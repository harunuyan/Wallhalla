package com.volie.wallhalla.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.volie.wallhalla.databinding.FragmentPhotoDetailsBinding

class PhotoDetailsFragment : Fragment() {
    private var _mBinding: FragmentPhotoDetailsBinding? = null
    private val mBinding get() = _mBinding!!
    private lateinit var args: PhotoDetailsFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentPhotoDetailsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            args = PhotoDetailsFragmentArgs.fromBundle(it)
        }

        getDetails()
    }

    private fun getDetails() {
        with(mBinding) {
            tvPhotographerName.text = args.info.photographer
            tvPhotographerUrl.text = args.info.photographer_url
            tvPhotographerUrl.setOnClickListener {
                val action =
                    PhotoDetailsFragmentDirections.actionPhotoDetailsFragmentToPhotographerFragment(
                        args.info.photographer_url
                    )
                findNavController().navigate(action)
            }
            Glide.with(requireContext())
                .load(args.info.src?.large2x)
                .into(ivPhotoDetails)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }
}