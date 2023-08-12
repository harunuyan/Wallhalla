package com.volie.wallhalla.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.volie.wallhalla.data.model.Photographer

class UserTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromUser(photographer: Photographer?): String? {
        return gson.toJson(photographer)
    }

    @TypeConverter
    fun toUser(userJson: String?): Photographer? {
        return gson.fromJson(userJson, Photographer::class.java)
    }
}