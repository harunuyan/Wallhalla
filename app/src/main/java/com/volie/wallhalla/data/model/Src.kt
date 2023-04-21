package com.volie.wallhalla.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Src(
    val landscape: String? = null,
    val large: String? = null,
    val large2x: String? = null,
    val medium: String? = null,
    val original: String? = null,
    val portrait: String? = null,
    val small: String? = null,
    val tiny: String? = null
) : Parcelable