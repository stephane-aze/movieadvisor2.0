package com.master.movieadvisor.service.providers

import android.util.Log
import com.master.movieadvisor.model.Category
import com.master.movieadvisor.model.Comment
import com.master.movieadvisor.model.Movie
import com.master.movieadvisor.model.PostComment
import com.master.movieadvisor.service.NetworkAPI
import com.master.movieadvisor.service.dto.CategoryDTO
import com.master.movieadvisor.service.dto.CommentDTO
import com.master.movieadvisor.service.dto.MoviesDTO
import com.master.movieadvisor.service.dto.SignInDTO
import com.master.movieadvisor.service.mapper.CategoryMapper
import com.master.movieadvisor.service.mapper.CommentMapper
import com.master.movieadvisor.service.mapper.MovieMapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val URL = "https://spring-movie-advisor.herokuapp.com/"

object NetworkProvider {
    private val networkAPI: NetworkAPI = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NetworkAPI::class.java)

    fun getMovies(listener: NetworkListener<List<Movie>>){
        networkAPI.getAllMovies().enqueue(object : Callback<List<MoviesDTO>>{
            override fun onFailure(call: Call<List<MoviesDTO>>, t: Throwable) {
                listener.onError(t)
            }

            override fun onResponse(call: Call<List<MoviesDTO>>, response: Response<List<MoviesDTO>>) {
                val moviesDto: List<MoviesDTO>? = response.body()
                moviesDto?.let { notNullMovieDto ->
                    //dto --> mapper --> model
                    val movies: List<Movie> = MovieMapper().map(notNullMovieDto)
                    listener.onSuccess(movies)
                } ?: listener.onError(Exception())
            }

        })
    }
    fun getCategories(listener: NetworkListener<List<Category>>){
        networkAPI.getAllCategories().enqueue(object : Callback<List<CategoryDTO>>{
            override fun onFailure(call: Call<List<CategoryDTO>>, t: Throwable) {
                listener.onError(t)
            }

            override fun onResponse(
                call: Call<List<CategoryDTO>>,
                response: Response<List<CategoryDTO>>
            ) {
                val categoriesDto: List<CategoryDTO>? = response.body()
                categoriesDto?.let { notNullCategoriesDto ->
                    //dto --> mapper --> model
                    val categories: List<Category> = CategoryMapper().map(notNullCategoriesDto)
                    listener.onSuccess(categories)
                } ?: listener.onError(Exception())
            }

        })
    }
    fun getOpinionsByMovie(idMovie:Int, listener: NetworkListener<List<Comment>>){
        networkAPI.getCommentsByMovie(id = idMovie).enqueue(object : Callback<List<CommentDTO>>{
            override fun onFailure(call: Call<List<CommentDTO>>, t: Throwable) {
                listener.onError(t)
            }

            override fun onResponse(
                call: Call<List<CommentDTO>>,
                response: Response<List<CommentDTO>>
            ) {
                val commentDto: List<CommentDTO>? = response.body()
                commentDto?.let { notNullCommentDto ->
                    val comments: List<Comment> = CommentMapper().map(notNullCommentDto)
                    listener.onSuccess(comments)
                } ?: listener.onError(Exception())
            }

        })
    }
    fun getOpinionsByUser(idUser:String, listener: NetworkListener<List<Comment>>){
        networkAPI.getCommentsByUser(id = idUser).enqueue(object : Callback<List<CommentDTO>>{
            override fun onFailure(call: Call<List<CommentDTO>>, t: Throwable) {
                listener.onError(t)
            }

            override fun onResponse(
                call: Call<List<CommentDTO>>,
                response: Response<List<CommentDTO>>
            ) {
                val commentDto: List<CommentDTO>? = response.body()
                Log.d("PSS",commentDto.toString())
                commentDto?.let { notNullCommentDto ->
                    val comments: List<Comment> = CommentMapper().map(notNullCommentDto)
                    listener.onSuccess(comments)
                } ?: listener.onError(Exception())
            }

        })
    }
    fun signIn(userSignIn: SignInDTO,listener: NetworkListener<String>){
        networkAPI.signIn(userSignIn).enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                listener.onError(t)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val responseString = response.body()
                    responseString?.let {
                        listener.onSuccess(it)
                    }
                }

            }


        })
    }
    fun postComment(comment: PostComment,listener: NetworkListener<String>){
        networkAPI.postComment(comment).enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                listener.onError(t)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val responseString = response.body()
                    responseString?.let {
                        listener.onSuccess(it)
                    }
                }

            }


        })
    }
    fun putComment(comment: Comment,listener: NetworkListener<Comment>){
        networkAPI.updateComment(idComment = comment.id,comment = comment).enqueue(object : Callback<CommentDTO>{


            override fun onFailure(call: Call<CommentDTO>, t: Throwable) {
                listener.onError(t)
            }

            override fun onResponse(call: Call<CommentDTO>, response: Response<CommentDTO>) {
                val commentDto: CommentDTO? = response.body()
                commentDto?.let {comment->
                    with(comment){
                        listener.onSuccess(
                            Comment(
                                id = id,
                                userId = userId ?: "",
                                comment = text ?: "",
                                like = isLiked,
                                rating = rating ?: 0.0,
                                movieId = movieId
                            )
                        )
                    }

                } ?: listener.onError(Exception())

            }


        })
    }
    fun removeComment(comment: Comment,listener: NetworkListener<Comment>){
        networkAPI.removeComment(idComment = comment.id).enqueue(object : Callback<String>{


            override fun onFailure(call: Call<String>, t: Throwable) {
                listener.onError(t)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {


            }


        })
    }

}
interface NetworkListener<T>{
    fun onSuccess(data: T)
    fun onError(throwable: Throwable)
}