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
    val landscape: String,
    val large2x: String,
) : Parcelable