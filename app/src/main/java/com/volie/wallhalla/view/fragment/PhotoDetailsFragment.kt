package com.volie.wallhalla.view.fragment

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.volie.wallhalla.R
import com.volie.wallhalla.databinding.FragmentPhotoDetailsBinding
import com.volie.wallhalla.view.viewmodel.PhotoDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
    private lateinit var args: PhotoDetailsFragmentArgs

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

        arguments?.let {
            args = PhotoDetailsFragmentArgs.fromBundle(it)
        }

        onBackPressed()

        with(mBinding) {
            ivBackDetails.setOnClickListener {
                findNavController().popBackStack()
            }
            ivFavDetails.setOnClickListener {
                if (!args.media.isLiked) {
                    mBinding.ivFavDetails.setImageResource(R.drawable.ic_favorited)
                    args.media.isLiked = true
                    mViewModel.savePhoto(args.media)
                } else {
                    mBinding.ivFavDetails.setImageResource(R.drawable.ic_fav)
                    args.media.isLiked = false
                    mViewModel.deletePhoto(args.media)
                }
            }
            ivDownloadDetails.setOnClickListener {
                downloadImage()
            }
            ivPhotoDetails.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Handler(Looper.myLooper()!!).postDelayed({
                            clPhotoDetails.visibility = View.GONE
                            ivBackDetails.visibility = View.GONE
                            ivInfoDetails.visibility = View.GONE
                        }, 200)
                    }

                    MotionEvent.ACTION_UP -> {
                        Handler(Looper.myLooper()!!).postDelayed({
                            clPhotoDetails.visibility = View.VISIBLE
                            ivBackDetails.visibility = View.VISIBLE
                            ivInfoDetails.visibility = View.VISIBLE
                        }, 100)
                    }
                }
                true
            }
        }

        getDetails()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun downloadImage() {
        if (!isNetworkAvailable()) {
            mBinding.ivDownloadDetails.setImageResource(R.drawable.ic_download_failed)
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(args.media.src?.large2x.toString())
                .build()

            val response = client.newCall(request).execute()

            val inputStream: InputStream? = response.body?.byteStream()
            val image = BitmapFactory.decodeStream(inputStream)

            val displayName = "Title"
            val mimeType = "image/jpeg"
            val contentResolver = context?.contentResolver
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
                        if (image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                            withContext(Dispatchers.Main) {
                                mBinding.ivDownloadDetails.setImageResource(R.drawable.ic_download_succesfully)
                                showDownloadNotification()
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
            tvPhotographerName.text = args.media.photographer
            tvPhotographerUrl.text = args.media.photographerUrl
            tvPhotographerUrl.setOnClickListener {
                val action =
                    PhotoDetailsFragmentDirections.actionPhotoDetailsFragmentToPhotographerFragment(
                        args.media.photographerUrl
                    )
                findNavController().navigate(action)
            }
            Glide.with(requireContext())
                .load(args.media.src?.large2x)
                .into(ivPhotoDetails)
        }
        if (args.media.isLiked) {
            mBinding.ivFavDetails.setImageResource(R.drawable.ic_favorited)
        } else {
            mBinding.ivFavDetails.setImageResource(R.drawable.ic_fav)
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
        _mBinding = null
    }
}