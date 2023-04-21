package com.volie.wallhalla.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val alt: String? = null,
    val avg_color: String? = null,
    val height: Int? = null,
    val id: Int? = null,
    val liked: Boolean? = null,
    val photographer: String? = null,
    val photographer_id: Int? = null,
    val photographer_url: String? = null,
    val src: Src? = null,
    val url: String? = null,
    val width: Int? = null
) : Parcelable