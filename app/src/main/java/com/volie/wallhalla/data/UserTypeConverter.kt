package com.volie.wallhalla.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.volie.wallhalla.data.model.User

class UserTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromUser(user: User?): String? {
        return gson.toJson(user)
    }

    @TypeConverter
    fun toUser(userJson: String?): User? {
        return gson.fromJson(userJson, User::class.java)
    }
}