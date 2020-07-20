package com.master.movieadvisor.service


import com.master.movieadvisor.model.Comment
import com.master.movieadvisor.model.PostComment
import com.master.movieadvisor.model.SignInModel
import com.master.movieadvisor.service.dto.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface NetworkAPI {
    @Headers("Content-Type: application/json")
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
    @Headers("Content-Type: application/json")
    @POST("/opinion/add")
    fun postComment(@Body comment: PostComment): Call<CommentDTO>
    @DELETE("/opinion/{id}")
    fun removeComment(@Path("id") id: Int): Call<String>
    @Headers("Content-Type: application/json")
    @PUT("/opinion/{id}")
    fun updateComment(@Body comment: Comment,@Path("id") id: Int): Call<CommentDTO>
}