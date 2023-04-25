package com.volie.wallhalla.data.model.collection

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoFile(
    val file_type: String? = null,
    val fps: Double? = null,
    val height: Int? = null,
    val id: Int? = null,
    val link: String? = null,
    val quality: String? = null,
    val width: Int? = null
) : Parcelable