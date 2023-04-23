package com.volie.wallhalla.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "curated_table")
@Parcelize
data class CuratedResponse(
    @PrimaryKey(autoGenerate = true)
    val curatedId: Int? = null,
    val next_page: String? = null,
    val page: Int? = null,
    val per_page: Int? = null,
    val photos: List<Photo?>? = null,
    val total_results: Int? = null
) : Parcelable