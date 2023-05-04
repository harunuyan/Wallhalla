package com.volie.wallhalla.data.service

import com.volie.wallhalla.data.model.CollectionMediaResponse
import com.volie.wallhalla.data.model.CollectionResponse
import com.volie.wallhalla.data.model.CuratedResponse
import com.volie.wallhalla.data.model.SearchResponse
import com.volie.wallhalla.util.Constant.AUTHORIZATION
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
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

    @GET("collections/featured")
    suspend fun getFeaturedCollections(
        @Query("page")
        page: Int? = 1,
        @Query("per_page")
        perPage: Int? = 40,
        @Header("Authorization")
        authorization: String = AUTHORIZATION
    ): Response<CollectionResponse>

    @GET("collections/{id}")
    suspend fun getCollections(
        @Path("id")
        id: String,
        @Query("page")
        page: Int? = 1,
        @Query("type")
        type: String? = "photos",
        @Query("per_page")
        perPage: Int? = 40,
        @Header("Authorization")
        authorization: String = AUTHORIZATION
    ): Response<CollectionMediaResponse>

    @GET("search")
    suspend fun getSearchResult(
        @Query("query")
        query: String?,
        @Query("page")
        page: Int? = 1,
        @Query("per_page")
        perPage: Int? = 40,
        @Header("Authorization")
        authorization: String = AUTHORIZATION
    ): Response<SearchResponse>
}