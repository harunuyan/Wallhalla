package com.volie.wallhalla.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "curated_table")
@Parcelize
data class CuratedResponse(
    @PrimaryKey(autoGenerate = true)
    val curatedId: Int? = null,
    @SerializedName("next_page")
    val nextPage: String? = null,
    val page: Int? = null,
    @SerializedName("per_page")
    val perPage: Int? = null,
    @SerializedName("photos")
    val media: List<Media?>? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null
) : Parcelable