package com.volie.wallhalla.view.fragment.detail

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.volie.wallhalla.R
import com.volie.wallhalla.data.model.Quality
import com.volie.wallhalla.data.model.WallpaperType
import com.volie.wallhalla.databinding.BottomSheetLayoutSelectScreenBinding
import com.volie.wallhalla.databinding.FragmentPhotoDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.io.InputStream

@AndroidEntryPoint
class PhotoDetailsFragment : Fragment() {

    private var _mBinding: FragmentPhotoDetailsBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: PhotoDetailsViewModel by viewModels()
    private val mArgs: PhotoDetailsFragmentArgs by navArgs()

    private var lastClickTime = 0L
    private var isAnimating = false
    private var selectedQuality = Quality.ORIGINAL
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentPhotoDetailsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBackPressed()

        val wallpaperUrl = mArgs.media.src?.large2x

        with(mBinding) {

            ivPhotoDetails.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val clickTime = System.currentTimeMillis()
                    if (clickTime - lastClickTime < 300) {
                        toggleLike()
                    }
                    lastClickTime = clickTime
                }
                true
            }

            ivBackDetails.setOnClickListener {
                findNavController().navigateUp()
            }

            ivSetWallpaper.setOnClickListener {
                setWallpaper()
            }

            ivFavDetails.setOnClickListener {
                toggleLike()
            }

            ivDownloadDetails.setOnClickListener {
                if (!mArgs.media.isDownloaded) {
                    mArgs.media.isDownloaded = true
                    downloadImage(wallpaperUrl!!)
                }
            }
        }

        getDetails()
        getQualityOptions()
    }

    private fun showHeartAnimation(imageViewHeart: LottieAnimationView) {
        imageViewHeart.visibility = View.VISIBLE
        imageViewHeart.playAnimation()
        if (mArgs.media.isLiked) {
            imageViewHeart.setAnimation("animation_heart.json")

        } else {
            imageViewHeart.setAnimation("animation_heart_break.json")
        }
        isAnimating = true
        imageViewHeart.playAnimation()

        job = CoroutineScope(Dispatchers.Main).launch {
            delay(1200)
            imageViewHeart.visibility = View.GONE
            imageViewHeart.cancelAnimation()
            isAnimating = false
        }
    }

    private fun toggleLike() {
        if (!mArgs.media.isLiked) {
            mBinding.ivFavDetails.setImageResource(R.drawable.ic_favorited)
            mArgs.media.isLiked = true
            mViewModel.savePhoto(mArgs.media)
        } else {
            mBinding.ivFavDetails.setImageResource(R.drawable.ic_fav)
            mArgs.media.isLiked = false
            mViewModel.deletePhoto(mArgs.media)
        }
        if (!isAnimating) {
            showHeartAnimation(mBinding.ivDoubleClickHeart)
        }
    }

    private fun setWallpaper() {

        val wallpaperUrl = mArgs.media.src?.large2x

        with(mBinding) {
            val bottomSheetDialog =
                BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
            val bottomSheetView = LayoutInflater.from(requireContext())
                .inflate(R.layout.bottom_sheet_layout_select_screen, root, false)
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()

            val mBindingBottomSheet =
                BottomSheetLayoutSelectScreenBinding.bind(bottomSheetView)

            with(mBindingBottomSheet) {

                tvHomeScreen.setOnClickListener {
                    mViewModel.setWallpaper(
                        wallpaperUrl,
                        WallpaperType.HOME_SCREEN,
                        requireContext()
                    )
                    bottomSheetDialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Wallpaper set successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                tvLockScreen.setOnClickListener {
                    mViewModel.setWallpaper(
                        wallpaperUrl,
                        WallpaperType.LOCK_SCREEN,
                        requireContext()
                    )
                    bottomSheetDialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Wallpaper set successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                tvBoth.setOnClickListener {
                    mViewModel.setWallpaper(wallpaperUrl, WallpaperType.BOTH, requireContext())
                    bottomSheetDialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Wallpaper set successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun downloadImage(url: String) {
        if (!isNetworkAvailable()) {
            mArgs.media.isDownloaded = false
            mBinding.ivDownloadDetails.setImageResource(R.drawable.ic_download_failed)
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {

            val client = OkHttpClient()

            val request = Request.Builder()
                .url(url)
                .build()

            val response = client.newCall(request).execute()

            val inputStream: InputStream? = response.body?.byteStream()
            val image = BitmapFactory.decodeStream(inputStream)

            val displayName = "Title"
            val mimeType = "image/jpeg"
            val contentResolver = requireContext().contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
                put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            }

            val uri = contentResolver?.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )

            var hasError = false
            uri?.let {
                contentResolver.openOutputStream(it, "w").use { outputStream ->
                    try {
                        if (image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream!!)) {
                            withContext(Dispatchers.Main) {
                                mArgs.media.isDownloaded = true
                                mBinding.ivDownloadDetails.setImageResource(R.drawable.ic_download_succesfully)
                                showDownloadNotification()
                                Toast.makeText(
                                    requireContext(),
                                    "Successfully downloaded",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            hasError = true
                        }
                    } catch (e: IOException) {
                        hasError = true
                    }
                }
            } ?: run {
                hasError = true
            }

            if (hasError) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Download failed.", Toast.LENGTH_SHORT).show()
                }
            }
            inputStream?.close()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun showDownloadNotification() {
        val notificationManager = ContextCompat.getSystemService(
            requireContext(), NotificationManager::class.java
        ) as NotificationManager

        val notificationId = 1
        val channelId = "channelId"
        val title = "Image downloaded successfully"

// Create a notification channel for Android 8.0 and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel name",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel description"
            }

            notificationManager.createNotificationChannel(channel)
        }

// Create a notification builder
        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.ic_download)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

// Show the notification
        notificationManager.notify(notificationId, builder.build())
    }

    private fun getDetails() {
        with(mBinding) {

            tvPhotographerName.text = mArgs.media.photographer
            tvPhotographerUrl.text = mArgs.media.photographerUrlToDisplay
            tvPhotographerUrl.setOnClickListener {
                val action =
                    PhotoDetailsFragmentDirections.actionPhotoDetailsFragmentToPhotographerFragment(
                        mArgs.media.photographerUrl
                    )
                findNavController().navigate(action)
            }
            ivInfoDetails.setOnClickListener {
                val action =
                    PhotoDetailsFragmentDirections.actionPhotoDetailsFragmentToPhotographerFragment(
                        mArgs.media.photographerUrl
                    )
                findNavController().navigate(action)
            }
        }

        if (mArgs.media.isLiked) {
            mBinding.ivFavDetails.setImageResource(R.drawable.ic_favorited)
        } else {
            mBinding.ivFavDetails.setImageResource(R.drawable.ic_fav)
        }

        if (mArgs.media.isDownloaded) {
            mBinding.ivDownloadDetails.setImageResource(R.drawable.ic_download_succesfully)
        } else {
            mBinding.ivDownloadDetails.setImageResource(R.drawable.ic_download)
        }
    }

    private fun mapQualityToUrl(quality: Quality): String {
        val url = mArgs.media.src!!
        return when (quality) {
            Quality.ORIGINAL -> url.original
            Quality.LARGE_2X -> url.large2x
            Quality.LARGE -> url.large
            Quality.MEDIUM -> url.medium
            Quality.SMALL -> url.small
            Quality.TINY -> url.tiny
            Quality.LANDSCAPE -> url.landscape
            Quality.PORTRAIT -> url.portrait
        }
    }

    private fun getQualityOptions() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.selectedQuality.collect {
                mapQualityToUrl(it).let { url ->
                    Glide.with(requireContext())
                        .load(url)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<
                                        Drawable>?, isFirstResource: Boolean
                            ): Boolean {
                                with(mBinding) {
                                    progressBar.visibility = View.GONE
                                    clPhotoDetails.visibility = View.GONE
                                }

                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<
                                        Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                with(mBinding) {
                                    progressBar.visibility = View.GONE
                                    clPhotoDetails.visibility = View.VISIBLE
                                }

                                return false
                            }
                        })
                        .into(mBinding.ivPhotoDetails)
                }
            }
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
        _mBinding = null
    }
}