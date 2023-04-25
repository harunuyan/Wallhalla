package com.volie.wallhalla.data.model.collection

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionFeedResponse(
    val id: String? = null,
    val media: List<Media?>? = null,
    val next_page: String? = null,
    val page: Int? = null,
    val per_page: Int? = null,
    val total_results: Int? = null
) : Parcelable