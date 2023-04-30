package com.volie.wallhalla.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.volie.wallhalla.data.model.Src

class SrcTypeConverter {

    @TypeConverter
    fun fromString(value: String?): Src? {
        if (value == null) {
            return null
        }
        val type = object : TypeToken<Src>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toString(src: Src?): String {
        if (src == null) {
            return ""
        }
        val type = object : TypeToken<Src>() {}.type
        return Gson().toJson(src, type)
    }
}