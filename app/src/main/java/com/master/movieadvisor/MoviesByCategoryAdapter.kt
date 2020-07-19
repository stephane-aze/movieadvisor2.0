package com.master.movieadvisor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.master.movieadvisor.model.Movie
import com.master.movieadvisor.model.MovieByCategory

import kotlinx.android.synthetic.main.list_movie_by_category.view.*

class MoviesByCategoryAdapter(val context: Context): RecyclerView.Adapter<CategoryViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()
    var listItemByCategory: List<MovieByCategory> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    //private val movieThumbnailAdapter by lazy { MovieThumbnailAdapter(context) }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CategoryViewHolder{
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_movie_by_category,parent,false)
        return CategoryViewHolder(inflater)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val movieByCategory: MovieByCategory = listItemByCategory[position]
        holder.textView.text = movieByCategory.title
        val childLayoutManager = LinearLayoutManager(holder.recyclerView.context, RecyclerView.HORIZONTAL, false)
        childLayoutManager.initialPrefetchItemCount = 4
        holder.recyclerView.apply {
            adapter = MovieThumbnailAdapter(movieByCategory.listMovies,context!!){ movieItem: Movie ->
                partItemClicked(movieItem)
            }
            setRecycledViewPool(viewPool)
        }/**/
    }
    private fun partItemClicked(movieItem : Movie) {
        val intent = Intent(context, MovieActivity::class.java)
        intent.putExtra("path",movieItem.posterPath)
        intent.putExtra("title",movieItem.title)
        intent.putExtra("description",movieItem.overview)
        intent.putExtra("vote",movieItem.voteAverage.toString())
        intent.putExtra("movieId",movieItem.id)
        context.startActivity(intent)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int = listItemByCategory.size

}
class CategoryViewHolder(itemView : View) :
    RecyclerView.ViewHolder(itemView) {
        val recyclerView : RecyclerView = itemView.listMovieForCategory
        val textView:TextView = itemView.title_category
    }
