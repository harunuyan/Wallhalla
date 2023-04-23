package com.volie.wallhalla.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.volie.wallhalla.data.model.Photo

@Dao
interface CuratedResponseDao {

    @Query("SELECT * FROM photo_table")
    suspend fun getWallpapers(): List<Photo>

    @Query("SELECT EXISTS(SELECT * FROM photo_table WHERE id = :id)")
    suspend fun isLiked(id: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: Photo)

    @Delete
    suspend fun deletePhoto(photo: Photo)
}