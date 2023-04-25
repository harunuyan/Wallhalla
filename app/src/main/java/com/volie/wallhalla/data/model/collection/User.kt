package com.volie.wallhalla.data.model.collection

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int? = null,
    val name: String? = null,
    val url: String? = null
) : Parcelable