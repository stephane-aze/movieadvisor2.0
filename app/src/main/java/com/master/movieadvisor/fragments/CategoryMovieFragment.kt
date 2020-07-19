package com.master.movieadvisor.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.internal.Mutable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.master.movieadvisor.MoviesByCategoryAdapter
import com.master.movieadvisor.R
import com.master.movieadvisor.helpers.*
import com.master.movieadvisor.model.Category
import com.master.movieadvisor.model.Movie
import com.master.movieadvisor.model.MovieByCategory
import com.master.movieadvisor.service.dto.SignInDTO
import com.master.movieadvisor.service.providers.NetworkListener
import com.master.movieadvisor.service.providers.NetworkProvider

import kotlinx.android.synthetic.main.fragment_category_movie_list.*




class CategoryMovieFragment : Fragment() {

    private var listCategory: List<Category> = emptyList()
    private var listMovies: List<Movie> = emptyList()
    private var listMoviesByCategory: MutableList<MovieByCategory> = mutableListOf()
    private val movieByCategoryAdapter by lazy { MoviesByCategoryAdapter(context!!) }
    override fun onResume() {
        super.onResume()
        getCategories()
        getMovies()
        NetworkProvider.signIn(SignInDTO(userIdAndroid = "aLii4CUvP8OvpQepLCEx0SdCpQb2",username = "Assali Antoine"),object : NetworkListener<String>{
            override fun onSuccess(data: String) {
                Log.d("OKI", "Okkkkkkk")
            }

            override fun onError(throwable: Throwable) {
                Log.e("Error", throwable.localizedMessage)
            }

        })


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_movie_list, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listMovieByCategoryView.apply {
                adapter=movieByCategoryAdapter

        }
    }
    private fun getCategories(){
        NetworkProvider.getCategories(listener = object : NetworkListener<List<Category>>{
            override fun onSuccess(data: List<Category>) {
                listCategory=data
            }

            override fun onError(throwable: Throwable) {
                Log.e("AZE",throwable.localizedMessage)

            }

        })

    }
    private fun getMovies() {

        NetworkProvider.getMovies(listener = object : NetworkListener<List<Movie>> {
            override fun onError(throwable: Throwable) {
                Log.e("Error", throwable.localizedMessage)
            }

            override fun onSuccess(data: List<Movie>) {
                //Log.d("Error", data.toString())
                listMovies=data
                createListMovieByCategory()
            }
        })
    }
    private fun createListMovieByCategory(){
        listCategory.forEach { category ->

            val listMoviesFilter= listMovies.filter {

                it.categories.contains(category.title)

            }

            if(listMoviesFilter.isNotEmpty()) {
                listMoviesByCategory.add(
                    MovieByCategory(category.title,
                        listMoviesFilter

                    )

                )
            }
        }
        movieByCategoryAdapter.listItemByCategory=listMoviesByCategory
    }
}
