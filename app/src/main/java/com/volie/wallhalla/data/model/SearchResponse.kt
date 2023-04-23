package com.volie.wallhalla.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "search_table")
@Parcelize
data class SearchResponse(
    @PrimaryKey(autoGenerate = true)
    val searchId: Int? = null,
    val totalResult: Int,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val next_page: Int
) : Parcelable