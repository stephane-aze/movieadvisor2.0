package com.master.movieadvisor.service.mapper


import com.master.movieadvisor.model.Comment
import com.master.movieadvisor.service.dto.CommentDTO

class CommentMapper {
    fun map(dto: List<CommentDTO>) = dto.map { mapComment(it) }

    private fun mapComment(commentDTO: CommentDTO): Comment {
        return with(commentDTO){
            Comment(id = id,userId = userId ?:"",comment = text?:"", isLiked = isLiked,note = rating?:0.0,movieId = movieId)
        }
    }
}