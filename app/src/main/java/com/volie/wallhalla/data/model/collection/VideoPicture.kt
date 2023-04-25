package com.volie.wallhalla.data.model.collection

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoPicture(
    val id: Int? = null,
    val nr: Int? = null,
    val picture: String? = null
) : Parcelable