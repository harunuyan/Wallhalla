package com.volie.wallhalla.data.model

data class CuratedResponse(
    val next_page: String? = null,
    val page: Int? = null,
    val per_page: Int? = null,
    val photos: List<Photo?>? = null,
    val total_results: Int? = null
)