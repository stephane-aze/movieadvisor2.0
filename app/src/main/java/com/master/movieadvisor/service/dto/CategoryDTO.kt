package com.master.movieadvisor.service.dto

import com.google.gson.annotations.SerializedName
import com.master.movieadvisor.model.Movie

data class CategoryDTO(
    @SerializedName("label") val title: String,
    @SerializedName("id") val id: Int
)
