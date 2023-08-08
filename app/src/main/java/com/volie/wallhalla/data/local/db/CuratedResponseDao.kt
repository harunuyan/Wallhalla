package com.volie.wallhalla.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.volie.wallhalla.data.model.Media

@Dao
interface CuratedResponseDao {

    @Query("SELECT * FROM media_table")
    suspend fun getWallpapers(): List<Media>

    @Query("SELECT EXISTS(SELECT * FROM media_table WHERE id = :id)")
    suspend fun isLiked(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: Media)

    @Delete
    suspend fun deletePhoto(photo: Media)
}