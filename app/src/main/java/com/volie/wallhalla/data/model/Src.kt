package com.volie.wallhalla.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "src_table")
@Parcelize
data class Src(
    @PrimaryKey(autoGenerate = false)
    val uuid: Int? = null,
    val landscape: String? = null,
    val large: String? = null,
    val large2x: String? = null,
    val medium: String? = null,
    val original: String? = null,
    val portrait: String? = null,
    val small: String? = null,
    val tiny: String? = null
) : Parcelable