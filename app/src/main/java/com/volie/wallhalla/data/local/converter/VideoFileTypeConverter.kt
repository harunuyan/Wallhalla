package com.volie.wallhalla.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.volie.wallhalla.data.model.VideoFile

class VideoFileTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromVideoFileList(videoFiles: List<VideoFile>?): String? {
        return gson.toJson(videoFiles)
    }

    @TypeConverter
    fun toVideoFileList(videoFilesJson: String?): List<VideoFile>? {
        val type = object : TypeToken<List<VideoFile>>() {}.type
        return gson.fromJson(videoFilesJson, type)
    }
}