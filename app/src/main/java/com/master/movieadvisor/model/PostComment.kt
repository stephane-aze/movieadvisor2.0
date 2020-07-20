package com.master.movieadvisor.model



data class PostComment (
    val userId: String,
    val comment: String,
    val movieId: Int,
    val note: Double,
    val isLiked: Boolean
    //val dateTime: LocalDateTime?
)