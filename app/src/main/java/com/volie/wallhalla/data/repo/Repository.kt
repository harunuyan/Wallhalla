package com.volie.wallhalla.data.repo

import com.volie.wallhalla.data.db.CuratedResponseDao
import com.volie.wallhalla.data.model.CuratedResponse
import com.volie.wallhalla.data.model.Photo
import com.volie.wallhalla.data.service.WallpaperService
import com.volie.wallhalla.util.Resource
import javax.inject.Inject

class Repository
@Inject constructor(
    private val service: WallpaperService,
    private val dao: CuratedResponseDao
) {

    suspend fun getWallpapers(page: Int): Resource<CuratedResponse> {
        return try {
            val response = service.getWallpapers(page = page)
            if (response.isSuccessful) {
                response.body()?.let {
                    it.photos?.forEach { photo ->
                        photo?.let {
                            photo.isLiked = isFavorite(photo.id)
                        }
                    }
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                Resource.error("An unknown error occured", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }

    suspend fun isFavorite(id: Long): Boolean {
        return dao.isLiked(id)
    }

    suspend fun getWallpapersFromDb(): List<Photo> {
        return dao.getWallpapers()
    }

    suspend fun insertCuratedResponse(photo: Photo) {
        dao.insertPhoto(photo)
    }

    suspend fun deleteCuratedResponse(photo: Photo) {
        dao.deletePhoto(photo)
    }
}