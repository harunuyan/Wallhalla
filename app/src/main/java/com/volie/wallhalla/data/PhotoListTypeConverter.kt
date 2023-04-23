package com.volie.wallhalla.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.volie.wallhalla.data.model.Photo

object PhotoListTypeConverter {

    @TypeConverter
    fun fromJson(json: String): List<Photo>? {
        return Gson().fromJson(json, object : TypeToken<List<Photo>>() {}.type)
    }

    @TypeConverter
    fun toJson(photos: List<Photo>?): String {
        return Gson().toJson(photos)
    }
}