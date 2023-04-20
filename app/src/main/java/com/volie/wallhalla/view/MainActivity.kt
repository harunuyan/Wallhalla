package com.volie.wallhalla.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.volie.wallhalla.R
import com.volie.wallhalla.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _mBinding: ActivityMainBinding? = null
    private val mBinding get() = _mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setupBottomNavView()
    }

    private fun setupBottomNavView() {
        mBinding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.feedFragment -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.feedFragment)
                    true
                }

                R.id.favoriteFragment -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.favoriteFragment)
                    true
                }

                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

}