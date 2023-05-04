package com.volie.wallhalla.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.volie.wallhalla.data.model.VideoFile

class VideoFileTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromVideoFile(videoFile: VideoFile?): String? {
        return gson.toJson(videoFile)
    }

    @TypeConverter
    fun toVideoFile(videoFileJson: String?): VideoFile? {
        return gson.fromJson(videoFileJson, VideoFile::class.java)
    }

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