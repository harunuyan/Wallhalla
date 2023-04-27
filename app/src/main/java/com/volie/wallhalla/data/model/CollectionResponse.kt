package com.volie.wallhalla.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionResponse(
    val collections: List<Collection>,
    @SerializedName("next_page")
    val nextPage: String,
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("total_result")
    val totalResults: Int
) : Parcelable