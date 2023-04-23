package com.volie.wallhalla.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.volie.wallhalla.data.PhotoListTypeConverter
import com.volie.wallhalla.data.SrcTypeConverter
import com.volie.wallhalla.data.model.CuratedResponse
import com.volie.wallhalla.data.model.Photo
import com.volie.wallhalla.data.model.SearchResponse
import com.volie.wallhalla.data.model.Src

@Database(
    entities = [CuratedResponse::class, Photo::class, SearchResponse::class, Src::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PhotoListTypeConverter::class, SrcTypeConverter::class)
abstract class CuratedResponseDatabase : RoomDatabase() {
    abstract fun getCuratedResponseDao(): CuratedResponseDao
}