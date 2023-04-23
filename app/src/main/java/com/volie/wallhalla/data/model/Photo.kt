package com.volie.wallhalla.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "photo_table")
@Parcelize
data class Photo(
    val alt: String? = null,
    val avg_color: String? = null,
    val height: Int? = null,
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    var isLiked: Boolean = false,
    val photographer: String? = null,
    val photographer_id: Int? = null,
    val photographer_url: String? = null,
    val src: Src? = null,
    val url: String? = null,
    val width: Int? = null
) : Parcelable