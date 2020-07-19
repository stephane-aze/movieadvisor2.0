package com.master.movieadvisor

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.master.movieadvisor.fragments.MessengerFragment
import com.master.movieadvisor.model.Comment
import com.master.movieadvisor.service.providers.NetworkListener
import com.master.movieadvisor.service.providers.NetworkProvider
import com.master.movieadvisor.ui.makeStatusBarTransparent
import kotlinx.android.synthetic.main.activity_movie.*


class MovieActivity:  AppCompatActivity()  {
    private var mTitleView: TextView? = null
    private var mDescriptionView: TextView? = null
    private var mButtonAddOpinion: Button? = null
    private var mNoteView: TextView? = null
    private lateinit var commentView: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var imageLike: ImageView
    private lateinit var imageDislike: ImageView

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        setContentView(R.layout.activity_movie)
        val bundle = intent.extras
        init()
        getParams(bundle)
        btn_create_opinion.setOnClickListener { openDialog() }
        updateFragment(bundle)
    }

    private fun updateFragment(bundle: Bundle?) {
        val arguments = Bundle()
        arguments.putInt("movieId", bundle?.getInt("movieId")!!)

        val messengerFragment =
            MessengerFragment()
        messengerFragment.arguments = arguments
        supportFragmentManager.beginTransaction().add(
            R.id.frame_fragment, messengerFragment
        ).commit()


    }

    private fun getParams(bundle: Bundle?) {
        val title = bundle?.getString("title")
        val description = bundle?.getString("description")
        val note = bundle?.getString("vote")
        mTitleView?.text = title
        mDescriptionView?.text = description
        mNoteView?.text = note
        init()
        val url = "https://image.tmdb.org/t/p/w500${bundle?.getString("path")}"
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.bobine_film)
            .into(findViewById(R.id.movie_image))
    }


    private fun init() {
        mTitleView = findViewById(R.id.movie_title)
        mDescriptionView = findViewById(R.id.movie_description)
        mNoteView = findViewById(R.id.movie_note)
        mButtonAddOpinion = findViewById(R.id.btn_create_opinion)


    }

    private fun openDialog(){
        val builder = AlertDialog.Builder(this,R.style.AlertDialogTheme)
        // Get the layout inflater
        val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_avis, null)
        commentView = view.findViewById(R.id.comment)
        ratingBar = view.findViewById(R.id.eval_user)
        imageDislike = view.findViewById(R.id.dislike)
        imageLike = view.findViewById(R.id.like)

        eventRatingBar()
        builder.setView(view)
            .setTitle(getString(R.string.title_dialog_rating))
            // Add action button
            .setPositiveButton(R.string.send)
            { _, _ ->
                val comment = commentView.text.toString()
                val rating = ratingBar.rating.toDouble()
                applyRating(rating, comment)

            }
            .setNegativeButton(R.string.cancel)
            { _, _ ->

            }
        builder.create().show()
    }

    private fun eventRatingBar() {
        ratingBar.setOnRatingBarChangeListener { _, v, b ->

            when (v>=2.5f) {
                true -> {
                    imageLike.visibility=View.VISIBLE
                    imageDislike.visibility=View.GONE
                }
                else -> {
                    imageLike.visibility=View.GONE
                    imageDislike.visibility=View.VISIBLE
                }
            }

        }
    }

    private fun applyRating(rating: Double, comment: String) {
        //Requete
    }

}