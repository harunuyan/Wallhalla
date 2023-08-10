package com.volie.wallhalla.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "src_table")
@Parcelize
data class Src(
    @PrimaryKey(autoGenerate = true)
    val srcId: Int? = null,
    val original: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String
) : Parcelable