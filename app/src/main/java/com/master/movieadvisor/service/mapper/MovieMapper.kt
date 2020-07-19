package com.master.movieadvisor.service.mapper


import com.master.movieadvisor.model.Movie
import com.master.movieadvisor.service.dto.MoviesDTO


class MovieMapper {

    fun map(dto: List<MoviesDTO>) = dto.map { mapMovie(it) }

    private fun mapMovie(movieDTO: MoviesDTO): Movie {
        return with(movieDTO){
            Movie(id = id?:0 ,title = title?:"",overview = description?:"",popularity = popularity?: 0.0,posterPath = poster_path?:"" ,voteAverage = note?: 0.0,releaseDate = date?:"",categories = categories?:"",averageCommentNote = averageCommentNote?:0.0,averageLikes = averageLikes?:2.0)
        }
    }
}