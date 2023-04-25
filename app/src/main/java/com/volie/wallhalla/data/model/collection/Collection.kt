package com.volie.wallhalla.data.model.collection

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Collection(
    val description: String? = null,
    val id: String? = null,
    val media_count: Int? = null,
    val photos_count: Int? = null,
    val `private`: Boolean? = null,
    val title: String? = null,
    val videos_count: Int? = null
) : Parcelable