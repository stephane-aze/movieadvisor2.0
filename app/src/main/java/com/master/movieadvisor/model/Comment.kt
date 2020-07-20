package com.master.movieadvisor.model

import com.google.gson.annotations.SerializedName


data class Comment (
    val id: Int,
    val userId: String,
    val comment: String,
    val movieId: Int,
    val note: Double,
    val isLiked: Boolean
    //val dateTime: LocalDateTime?
)