package com.volie.wallhalla.data.model.collection

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    val alt: String? = null,
    val avg_color: String? = null,
    val duration: Int? = null,
    val height: Int? = null,
    val id: Int? = null,
    val image: String? = null,
    val liked: Boolean? = null,
    val photographer: String? = null,
    val photographer_id: Int? = null,
    val photographer_url: String? = null,
    val src: Src? = null,
    val type: String? = null,
    val url: String? = null,
    val user: User? = null,
    val video_files: List<VideoFile?>? = null,
    val video_pictures: List<VideoPicture?>? = null,
    val width: Int? = null
) : Parcelable