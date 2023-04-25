package com.volie.wallhalla.data.model.collection

data class CollectionResponse(
    val collections: List<Collection?>? = null,
    val collectionFeedResponse: CollectionFeedResponse? = null,
    val next_page: String? = null,
    val page: Int? = null,
    val per_page: Int? = null,
    val total_results: Int? = null
)