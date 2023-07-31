package com.volie.wallhalla.view.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.volie.wallhalla.R
import com.volie.wallhalla.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _mBinding: ActivityMainBinding? = null
    private val mBinding get() = _mBinding!!
    private lateinit var reviewManager: ReviewManager
    private lateinit var reviewInfo: ReviewInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _mBinding = ActivityMainBinding.inflate(layoutInflater)
        val sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedTheme = sharedPrefs.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        AppCompatDelegate.setDefaultNightMode(savedTheme)
        setContentView(mBinding.root)

        requestReviewInfo()

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
                    R.id.videoPlayWebFragment -> View.GONE
                    R.id.splashScreenFragment -> View.GONE
                    else -> View.VISIBLE
                }
            }

        }
    }

    private fun requestReviewInfo() {
        reviewManager = ReviewManagerFactory.create(this)
        val request: Task<ReviewInfo> = reviewManager.requestReviewFlow()

        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                reviewInfo = task.result
            } else {
                @ReviewErrorCode val reviewErrorCode = (task.exception as ReviewException).errorCode
                Log.e("Review not successfully", "$reviewErrorCode")
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