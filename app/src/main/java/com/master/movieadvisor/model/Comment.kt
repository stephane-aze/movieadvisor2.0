package com.master.movieadvisor.model


data class Comment (
    val id: Int,
    val userId: String,
    val comment: String,
    val movieId: Int,
    val rating: Double,
    val like: Boolean
    //val dateTime: LocalDateTime?
)