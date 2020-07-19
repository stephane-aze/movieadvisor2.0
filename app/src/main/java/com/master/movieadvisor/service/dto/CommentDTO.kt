package com.master.movieadvisor.service.dto

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

data class CommentDTO (
    val id: Int,
    @SerializedName("userId") val userId: String?,
    @SerializedName("note") val rating: Double?,
    @SerializedName("comment") val text: String?,
    @SerializedName("isLiked") val isLiked: Boolean,
   // @SerializedName("datetime") val dateFormat: SimpleDateFormat?,
    @SerializedName("movieId") val movieId: Int
)
