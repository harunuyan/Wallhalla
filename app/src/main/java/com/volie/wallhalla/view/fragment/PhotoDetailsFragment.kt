package com.volie.wallhalla.view.fragment

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
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.volie.wallhalla.R
import com.volie.wallhalla.databinding.FragmentPhotoDetailsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.io.InputStream

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

        with(mBinding) {
            ivBackDetails.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        mBinding.ivFavDetails.setOnClickListener {
            if (args.info.isLiked) {
                mBinding.ivFavDetails.setImageResource(R.drawable.ic_fav)
                args.info.isLiked = false
            } else {
                mBinding.ivFavDetails.setImageResource(R.drawable.ic_favorited)
                args.info.isLiked = true
            }
        }

        arguments?.let {
            args = PhotoDetailsFragmentArgs.fromBundle(it)
        }

        mBinding.ivDownloadDetails.setOnClickListener {
            downloadImage()
        }

        getDetails()
    }

    private fun downloadImage() {
        if (!isNetworkAvailable()) {
            mBinding.ivDownloadDetails.setImageResource(R.drawable.ic_download_failed)
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(args.info.src?.large2x.toString())
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
        if (args.info.isLiked) {
            mBinding.ivFavDetails.setImageResource(R.drawable.ic_favorited)
        } else {
            mBinding.ivFavDetails.setImageResource(R.drawable.ic_fav)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }
}