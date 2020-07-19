package com.master.movieadvisor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.master.movieadvisor.model.Movie


class MovieThumbnailAdapter (private val children : List<Movie>, val context: Context, private val clickListener: (Movie) -> Unit)
    : RecyclerView.Adapter<MovieThumbnailAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {

        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.thumbnail_movie,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return children.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val child = children[position]
        holder.bind(child,clickListener)
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        private val textView : TextView = itemView.findViewById(R.id.thumbnail_title_movie)
        private val imageView: ImageView = itemView.findViewById(R.id.thumbnail_image_movie)

        @SuppressLint("ClickableViewAccessibility")
        fun bind(movie: Movie, clickListener: (Movie) -> Unit) {
            textView.text = movie.title
            val url =  "https://image.tmdb.org/t/p/w500${movie.posterPath}"
            Glide.with(itemView)
                .load(url)
                .placeholder(R.drawable.bobine_film)
                .into(imageView)

            itemView.setOnClickListener { clickListener(movie)}
            itemView.setOnTouchListener { v, _ ->
                val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.zoom_in)
                v.startAnimation(animation)
                false
            }
        }

    }
}