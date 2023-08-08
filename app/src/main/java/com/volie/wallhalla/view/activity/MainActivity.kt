package com.volie.wallhalla.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.volie.wallhalla.R
import com.volie.wallhalla.data.local.data_store.DataStoreRepository
import com.volie.wallhalla.data.model.Theme
import com.volie.wallhalla.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    private var _mBinding: ActivityMainBinding? = null
    private val mBinding get() = _mBinding!!
    private lateinit var reviewManager: ReviewManager
    private lateinit var reviewInfo: ReviewInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setTheme()
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

    private fun setTheme() {
        lifecycleScope.launch {
            dataStoreRepository.selectedThemeFlow.collect { savedTheme ->
                when (savedTheme) {
                    Theme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    Theme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    Theme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
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

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

}