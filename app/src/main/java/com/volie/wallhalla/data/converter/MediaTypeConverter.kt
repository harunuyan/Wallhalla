package com.volie.wallhalla.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.volie.wallhalla.data.model.Media

class MediaTypeConverter {

    @TypeConverter
    fun fromJson(json: String): List<Media>? {
        return Gson().fromJson(json, object : TypeToken<List<Media>>() {}.type)
    }

    @TypeConverter
    fun toJson(media: List<Media>?): String {
        return Gson().toJson(media)
    }
}