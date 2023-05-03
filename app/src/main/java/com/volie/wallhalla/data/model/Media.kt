package com.volie.wallhalla.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "media_table")
@Parcelize
data class Media(
    val type: String? = null,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val url: String,
    val photographer: String? = null,
    @SerializedName("photographer_url")
    val photographerUrl: String? = null,
    @SerializedName("avg_color")
    val avgColor: String? = "#FFFFFF",
    val src: Src? = null,
    var isLiked: Boolean,
    val image: String? = null,
    val user: User? = null,
    @SerializedName("video_files")
    val videoFiles: List<VideoFile>? = null,
) : Parcelable {

    val photographerUrlToDisplay: String
        get() = photographerUrl?.trim()!!.removePrefix("https://www.pexels.com/")

}