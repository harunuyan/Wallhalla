package com.volie.wallhalla.data.model.collection

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionResponse(
    val collections: List<Collection?>? = null,
    val collectionFeedResponse: CollectionFeedResponse? = null,
    val next_page: String? = null,
    val page: Int? = null,
    val per_page: Int? = null,
    val total_results: Int? = null
) : Parcelable