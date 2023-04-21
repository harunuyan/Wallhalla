package com.volie.wallhalla.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResponse(
    val totalResult: Int,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val next_page: Int
) : Parcelable