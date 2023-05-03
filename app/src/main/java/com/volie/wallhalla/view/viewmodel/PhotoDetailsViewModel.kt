package com.volie.wallhalla.view.viewmodel

import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volie.wallhalla.data.model.Media
import com.volie.wallhalla.data.model.WallpaperType
import com.volie.wallhalla.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel
@Inject constructor(
    private val repository: Repository

) : ViewModel() {

    fun savePhoto(photo: Media) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPhoto(photo)
        }
    }

    fun deletePhoto(photo: Media) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePhoto(photo)

        }
    }

    fun setWallpaper(url: String?, wallpaperType: WallpaperType, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            url?.let {
                val wallpaperManager = WallpaperManager.getInstance(context)
                val stream = URL(url).openStream()

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        when (wallpaperType) {
                            WallpaperType.HOME_SCREEN -> wallpaperManager.setStream(
                                stream,
                                null,
                                true,
                                WallpaperManager.FLAG_SYSTEM
                            )

                            WallpaperType.LOCK_SCREEN -> wallpaperManager.setStream(
                                stream,
                                null,
                                true,
                                WallpaperManager.FLAG_LOCK
                            )

                            WallpaperType.BOTH -> wallpaperManager.setStream(
                                stream,
                                null,
                                true,
                                WallpaperManager.FLAG_LOCK or WallpaperManager.FLAG_SYSTEM
                            )
                        }
                    } else {
                        wallpaperManager.setStream(stream)
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed to set wallpaper", Toast.LENGTH_SHORT)
                        .show()
                } finally {
                    try {
                        stream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}