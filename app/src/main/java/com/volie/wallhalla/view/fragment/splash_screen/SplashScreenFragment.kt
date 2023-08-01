package com.volie.wallhalla.view.fragment.splash_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.volie.wallhalla.R
import com.volie.wallhalla.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {
    private var _mBinding: FragmentSplashScreenBinding? = null
    private val mBinding get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentSplashScreenBinding.inflate(inflater, container, false)


        lifecycleScope.launch {

            Glide.with(requireContext())
                .load(R.drawable.head)
                .into(mBinding.ivSplashScreenGif)

            delay(1000)

            withContext(Dispatchers.Main) {
                val action =
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }
        return mBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}