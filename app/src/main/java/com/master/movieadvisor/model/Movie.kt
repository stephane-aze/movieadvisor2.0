package com.master.movieadvisor.model

import com.master.movieadvisor.SearchAdapter
import java.util.*

data class Movie(val id:Int,
                 val title: String,
                 val overview: String,
                 val voteAverage: Double,
                 val posterPath: String,
                 val popularity: Double,
                 val releaseDate: String,
                 val categories: String,
                 val averageLikes: Double,
                 val averageCommentNote: Double
                 )
   /* :
    SearchAdapter.Searchable {
    override fun getSearchCriteria(): String {
        return title.toLowerCase(Locale.ROOT)
    }
}*/