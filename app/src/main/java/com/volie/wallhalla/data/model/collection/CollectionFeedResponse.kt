package com.volie.wallhalla.data.model.collection

data class CollectionFeedResponse(
    val collections: List<Collection?>? = null,
    val next_page: String? = null,
    val page: Int? = null,
    val per_page: Int? = null,
    val total_results: Int? = null
)