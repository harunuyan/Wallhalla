package com.volie.wallhalla.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photographer(
    val name: String,
    val url: String
) : Parcelable