package com.volie.wallhalla.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "search_table")
@Parcelize
data class SearchResponse(
    @PrimaryKey(autoGenerate = true)
    val searchId: Int? = null,
    val totalResult: Int,
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val photos: List<Media>,
    @SerializedName("next_page")
    val nextPage: Int
) : Parcelable