package com.volie.wallhalla.data.model

data class User(
    val uid: String? = null,
    val fullName: String? = null,
    val username: String? = null,
    val email: String? = null,
    val profilePic: String? = null,
) {
    fun toMap() = mapOf(
        "userId" to uid,
        "name" to fullName,
        "username" to username,
        "imageUrl" to profilePic,
    )
}
