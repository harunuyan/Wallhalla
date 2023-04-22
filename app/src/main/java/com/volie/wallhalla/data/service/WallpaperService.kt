package com.volie.wallhalla.data.service

import com.volie.wallhalla.data.model.CuratedResponse
import com.volie.wallhalla.util.Constant.AUTHORIZATION
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WallpaperService {

    @GET("curated")
    suspend fun getWallpapers(
        @Query("page")
        page: Int? = 1,
        @Query("per_page")
        perPage: Int? = 40,
        @Header("Authorization")
        authorization: String = AUTHORIZATION
    ): Response<CuratedResponse>
}