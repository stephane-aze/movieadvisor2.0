package com.master.movieadvisor.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.chip.ChipGroup
import com.master.movieadvisor.ListViewFilm
import com.master.movieadvisor.MovieActivity
import com.master.movieadvisor.R
import com.master.movieadvisor.model.Movie
import com.master.movieadvisor.service.providers.NetworkListener
import com.master.movieadvisor.service.providers.NetworkProvider
import kotlinx.android.synthetic.main.fragment_movies_list.*
import java.util.*


class SearchFragment : Fragment() {

    private lateinit var searchMovieView: SearchView
    private val searchAdapter by lazy { ListViewFilm() }
    private lateinit var chipGroup: ChipGroup
    private lateinit var spinner: Spinner
    private var listMovies: List<Movie> = emptyList()
    override fun onResume() {
        super.onResume()
        getData()
    }
    private fun getData() {

        NetworkProvider.getMovies(listener = object : NetworkListener<List<Movie>> {
            override fun onError(throwable: Throwable) {
                Log.e("Error", throwable.localizedMessage)
            }

            override fun onSuccess(data: List<Movie>) {
                //Log.d("Error", data.toString())
               listMovies=data
                searchAdapter.listItem = listMovies
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
        // Inflate the layout for this fragment
       return inflater.inflate(R.layout.fragment_movies_list, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)
        initView(view)
        val itemsSpinner= arrayListOf(getString(R.string.year))
        val yearCurrent=Calendar.getInstance().get(Calendar.YEAR)
        for(x in  yearCurrent downTo  1988){
            itemsSpinner.add(x.toString())

        }

        val arrayAdapter =ArrayAdapter<String>(context!!, R.layout.custom_spinner, itemsSpinner)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter=arrayAdapter

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                if(position > 0){
                    val textSelected = itemsSpinner[position]
                    searchByYears(textSelected)

                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

    eventsCheckedChip()
        listMoviesView.apply {
            adapter = searchAdapter

       }
        searchAdapter.clickListener = { movieItem: Movie ->
            partItemClicked(movieItem)
        }
        searchAdapter.listItem = listMovies
        searchMovie(view)

    }

    private fun initView(view: View) {
        chipGroup = view.findViewById(R.id.chip_group)
        spinner = view.findViewById(R.id.spinner_years)
    }

    private fun eventsCheckedChip() {
        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.selected_view -> sortBy(1)
                R.id.selected_rating -> sortBy(2)
                R.id.selected_likes -> sortBy(3)
                R.id.selected_comment -> sortBy(4)
                else -> {
                    searchAdapter.listItem=searchAdapter.listItem.sortedBy { it.title }
                }
            }
        }
    }

    private fun sortBy(indicator:Int) {
        val listCurrent = searchAdapter.listItem
        val listSorted=listCurrent.sortedByDescending {
            when(indicator){
                1->  it.popularity
                2->  it.voteAverage
                3 -> it.averageLikes
                else -> it.averageCommentNote
            }
        }

        searchAdapter.listItem=listSorted
    }

    private fun searchMovie(view: View) {

        searchMovieView=view.findViewById(R.id.search_movie)
        searchMovieView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return false
            }

        })


    }

    private fun partItemClicked(movieItem : Movie) {
        val intent = Intent(context, MovieActivity::class.java)
        intent.putExtra("path",movieItem.posterPath)
        intent.putExtra("title",movieItem.title)
        intent.putExtra("description",movieItem.overview)
        intent.putExtra("vote",movieItem.voteAverage.toString())
        intent.putExtra("movieId",movieItem.id)
        startActivity(intent)
    }
    private fun search(s: String?) {
        searchAdapter.listItem=listMovies.filter {
            it.title.toLowerCase(
                Locale.ROOT).contains(s!!.toLowerCase(
                Locale.ROOT))
        }


    }
    private fun searchByYears(s: String) {
        if(s.isNotBlank()){
            searchAdapter.listItem=searchAdapter.listItem.filter {
                it.releaseDate.contains(s)
            }

        }else{
            searchAdapter.listItem=listMovies
        }
    }

}
