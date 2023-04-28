package com.volie.wallhalla.data.repo

import com.volie.wallhalla.data.db.CuratedResponseDao
import com.volie.wallhalla.data.model.CollectionMediaResponse
import com.volie.wallhalla.data.model.CollectionResponse
import com.volie.wallhalla.data.model.CuratedResponse
import com.volie.wallhalla.data.model.Media
import com.volie.wallhalla.data.service.WallpaperService
import com.volie.wallhalla.util.Resource
import javax.inject.Inject

class Repository
@Inject constructor(
    private val service: WallpaperService,
    private val dao: CuratedResponseDao
) {

    suspend fun getWallpapersFromRemote(page: Int): Resource<CuratedResponse> {
        return try {
            val response = service.getWallpapers(page = page)
            if (response.isSuccessful) {
                response.body()?.let {
                    it.media?.forEach { media ->
                        media?.let {
                            media.isLiked = isFavorite(media.id)
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

    suspend fun getFeaturedCollections(page: Int): Resource<CollectionResponse> {
        return try {
            val response = service.getFeaturedCollections(page)
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

    suspend fun getCollectionPhotos(id: String, page: Int): Resource<CollectionMediaResponse> {
        return try {
            val response = service.getCollections(id = id, page = page)
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

    private suspend fun isFavorite(id: Int): Boolean {
        return dao.isLiked(id)
    }

    suspend fun getWallpapersFromDb(): List<Media> {
        return dao.getWallpapers()
    }

    suspend fun insertPhoto(photo: Media) {
        dao.insertPhoto(photo)
    }

    suspend fun deletePhoto(photo: Media) {
        dao.deletePhoto(photo)
    }
}