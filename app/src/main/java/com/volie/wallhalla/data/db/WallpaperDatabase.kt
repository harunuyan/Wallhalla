package com.volie.wallhalla.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.volie.wallhalla.data.converter.MediaTypeConverter
import com.volie.wallhalla.data.converter.SrcTypeConverter
import com.volie.wallhalla.data.converter.UserTypeConverter
import com.volie.wallhalla.data.converter.VideoFileTypeConverter
import com.volie.wallhalla.data.model.CuratedResponse
import com.volie.wallhalla.data.model.Media
import com.volie.wallhalla.data.model.SearchResponse
import com.volie.wallhalla.data.model.Src

@Database(
    entities = [
        CuratedResponse::class,
        Media::class,
        SearchResponse::class,
        Src::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    MediaTypeConverter::class,
    SrcTypeConverter::class,
    VideoFileTypeConverter::class,
    UserTypeConverter::class
)
abstract class WallpaperDatabase : RoomDatabase() {
    abstract fun getCuratedResponseDao(): CuratedResponseDao
}