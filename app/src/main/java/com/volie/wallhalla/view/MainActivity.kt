package com.volie.wallhalla.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
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
        val sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedTheme = sharedPrefs.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        AppCompatDelegate.setDefaultNightMode(savedTheme)
        setContentView(mBinding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        mBinding.bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            with(mBinding.bottomNavView) {
                visibility = when (destination.id) {
                    R.id.photoDetailsFragment -> View.GONE
                    R.id.photographerFragment -> View.GONE
                    R.id.web_view_collection_video -> View.GONE
                    R.id.splashScreenFragment -> View.GONE
                    else -> View.VISIBLE
                }
            }

        }
    }

    fun saveTheme(theme: Int) {
        val sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putInt("theme", theme).apply()
    }

    fun getSavedTheme(): Int {
        val sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

}