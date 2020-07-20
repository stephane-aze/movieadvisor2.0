package com.master.movieadvisor.model


data class MessagesViewModel(
    val userImg: String? =null,
    val userName: String,
    val rating: Double,
    val text: String,
    val isLiked: Boolean,
    //val datetime: String,
    val movieId: Int,
    val id: Int
)