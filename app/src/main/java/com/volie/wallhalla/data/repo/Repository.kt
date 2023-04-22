package com.volie.wallhalla.data.repo

import com.volie.wallhalla.data.model.CuratedResponse
import com.volie.wallhalla.data.service.WallpaperService
import com.volie.wallhalla.util.Resource
import javax.inject.Inject

class Repository
@Inject constructor(
    private val service: WallpaperService,
) {

    suspend fun getWallpapers(): Resource<CuratedResponse> {
        return try {
            val response = service.getWallpapers()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                Resource.error("An unknown error occured", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }
}