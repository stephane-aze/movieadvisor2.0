package com.master.movieadvisor.service.dto

import com.google.gson.annotations.SerializedName

data class MoviesDTO(
    @SerializedName("id") val id:Int?,
    @SerializedName("name") val title:String?,
    @SerializedName("description") val description:String?,
    @SerializedName("averageNote") val note:Double?,
    @SerializedName("image") val poster_path: String?,
    @SerializedName("popularity") val popularity: Double?,
    @SerializedName("releaseDate") val date: String?,
    @SerializedName("categories") val categories: String?,
    @SerializedName("averageLikes") val averageLikes: Double?,
    @SerializedName("averageCommentNote") val averageCommentNote: Double?

)
