package com.master.movieadvisor
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.master.movieadvisor.model.Movie


class ListViewFilm:RecyclerView.Adapter<MovieViewHolder>() {
    lateinit var clickListener: ((Movie) -> Unit)

    //activity --> transférer WeaponList --> setWeapons( weapons)
    var listItem: List<Movie> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MovieViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater, parent)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: Movie = listItem[position]
        holder.bind(movie,clickListener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int = listItem.size

}

class MovieViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_movie, parent, false)) {
    private var mTitleView: TextView = itemView.findViewById(R.id.list_title)
    private var mDescriptionView: TextView = itemView.findViewById(R.id.list_description)
    private var mNoteView: TextView= itemView.findViewById(R.id.list_note)
    private var mImageView: ImageView= itemView.findViewById(R.id.list_image)




    fun bind(movie: Movie, clickListener: (Movie) -> Unit) {
        mTitleView.text = movie.title
        mDescriptionView.text = movie.overview
        itemView.setOnClickListener { clickListener(movie)}
        mNoteView.text = movie.voteAverage.toString()
        val url = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
        Glide.with(itemView.context)
            .load(url)
            .placeholder(R.drawable.bobine_film)
            .into(mImageView)


    }

}