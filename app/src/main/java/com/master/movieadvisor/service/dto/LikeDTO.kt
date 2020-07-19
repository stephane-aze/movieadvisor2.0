package com.master.movieadvisor.service.dto

import com.google.gson.annotations.SerializedName

data class LikeDTO (
    @SerializedName("user_id") val userId:Int?,
    @SerializedName("movie_id") val movieId:Int?
)