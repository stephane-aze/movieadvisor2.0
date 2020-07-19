package com.master.movieadvisor.service


import com.master.movieadvisor.model.Comment
import com.master.movieadvisor.model.SignInModel
import com.master.movieadvisor.service.dto.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface NetworkAPI {
    @POST("/user/add")
    fun signIn(@Body info: SignInDTO): Call<String>
    @GET("movie/all")
    fun getAllMovies():Call<List<MoviesDTO>>
    @GET("category/all")
    fun getAllCategories():Call<List<CategoryDTO>>
    @GET("/opinion/movie/{id}")
    fun getCommentsByMovie(@Path("id") id:Int):Call<List<CommentDTO>>
    @GET("/opinion/user/{id}")
    fun getCommentsByUser(@Path("id") id:String):Call<List<CommentDTO>>
    @POST("/opinion/add")
    fun postComment(@Body comment: Comment): Call<CommentDTO>
    @DELETE("comment/{id}")
    fun removeComment(@Path("id") idComment: Int): Call<CommentDTO>
    @PUT("comment/{id}")
    fun updateComment(@Body comment: Comment,@Path("id") idComment: Int): Call<CommentDTO>
}