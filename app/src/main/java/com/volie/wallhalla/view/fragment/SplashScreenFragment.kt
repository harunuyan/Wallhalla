package com.volie.wallhalla.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volie.wallhalla.databinding.FragmentSplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {
    private var _mBinding: FragmentSplashScreenBinding? = null
    private val mBinding get() = _mBinding!!
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        with(mBinding) {
            with(handler) {
                postDelayed({
                    ivSplashScreen2.visibility = View.VISIBLE
                }, 1000)

                postDelayed({
                    ivSplashScreenVegvisir.visibility = View.VISIBLE
                }, 1500)

                postDelayed({
                    val action =
                        SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment()
                    findNavController().navigate(action)
                }, 1500)
            }
        }
        return mBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}