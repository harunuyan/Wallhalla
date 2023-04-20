package com.volie.wallhalla.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}