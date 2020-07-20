package com.master.movieadvisor

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.master.movieadvisor.model.Comment
import com.master.movieadvisor.model.MessagesViewModel
import com.master.movieadvisor.model.PostComment
import com.master.movieadvisor.service.providers.NetworkListener
import com.master.movieadvisor.service.providers.NetworkProvider
import com.master.movieadvisor.ui.makeStatusBarTransparent
import kotlinx.android.synthetic.main.activity_movie.*


class MovieActivity:  AppCompatActivity()  {
    private lateinit var mTitleView: TextView
    private lateinit var mDescriptionView: TextView
    private lateinit var mButtonAddOpinion: Button
    private lateinit var buttonPositive: Button
    private lateinit var mNoteView: TextView
    private lateinit var commentView: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var imageLike: ImageView
    private lateinit var imageDislike: ImageView
    private lateinit var listMessage: RecyclerView
    private var comment: String? = null
    private var rating: Double?=null
    private var isLiked: Boolean?=null
    private var enableStatusChangeListener: Boolean?=null
    private val messagesAdapter by lazy { ListMessagesAdapter() }
    private var movieId: Int? = null
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        setContentView(R.layout.activity_movie)
        val bundle = intent.extras
        movieId=bundle!!.getInt("movieId")
        auth = Firebase.auth
        currentUser = auth.currentUser

        init()
        getParams(bundle)
        btn_create_opinion.setOnClickListener { openDialog() }
        //updateFragment(bundle)
    }

    override fun onResume() {
        super.onResume()
        currentUser?.let { user ->
            movieId?.let { movieId ->

                getOpinionsByMovie(movieId, user)
            }
        }
    }

    private fun getOpinionsByMovie(movieId: Int, user: FirebaseUser) {
        NetworkProvider.getOpinionsByMovie(idMovie = movieId, listener = object :
            NetworkListener<List<Comment>> {
            override fun onSuccess(data: List<Comment>) {
                val res=data.find { it.userId == user.uid }

                enableStatusChangeListener = res == null
                with(mButtonAddOpinion){
                    enableStatusChangeListener?.let {
                        isEnabled=it

                        alpha= if(!isEnabled)0.2f else 1f

                    }

                }
                val transformData = data.map { comment ->

                    MessagesViewModel(
                        userName = user.displayName ?: "Me",
                        rating = comment.note,
                        movieId = comment.movieId,
                        text = comment.comment,
                        isLiked = comment.isLiked,
                        id = comment.id
                    )
                }
                messagesAdapter.listItem = transformData as MutableList<MessagesViewModel>
            }

            override fun onError(throwable: Throwable) {
                Log.e("Error", throwable.localizedMessage)
            }

        })
    }

    private fun getParams(bundle: Bundle?) {
        val title = bundle?.getString("title")
        val description = bundle?.getString("description")
        val note = bundle?.getString("vote")
        mTitleView.text = title
        mDescriptionView.text = description
        mNoteView.text = note
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

        listMessage=findViewById(R.id.listMessageView)
        listMessage.adapter =messagesAdapter
        listMessage.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))


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

                val newComment= PostComment(movieId = movieId!!,note = rating!!,comment = comment!!,userId = currentUser!!.uid,isLiked = isLiked!!)
                applyRating(newComment)
                Log.d("edazfa", newComment.toString())

            }
            .setNegativeButton(R.string.cancel)
            { _, _ ->

            }
        val alertDialog=builder.create()
            alertDialog.show()
        buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        buttonPositive.isEnabled=false
        buttonPositive.alpha = 0.2f
        commentView.doAfterTextChanged {
            comment = it.toString()
            checkFields()
        }

    }

    private fun checkFields() {
        if (!comment.isNullOrEmpty() && rating!=null && isLiked!=null) {
            buttonPositive.isEnabled = true
            buttonPositive.alpha = 1f
        } else {
            buttonPositive.isEnabled = false
            buttonPositive.alpha = 0.2f
        }
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
            rating=v.toDouble()
            isLiked=imageLike.visibility==View.VISIBLE

            checkFields()

        }
    }

    private fun applyRating(comment: PostComment) {
        //Requete
        Log.d("PLS",comment.toString())
        NetworkProvider.postComment(comment,listener = object: NetworkListener<Comment>{
            override fun onSuccess(data: Comment) {
                Log.d("Envoi",data.toString())
                /*auth = Firebase.auth
                currentUser = auth.currentUser*/
                currentUser?.let { it ->
                    messagesAdapter.listItem.add(
                        MessagesViewModel(
                            userName = it.displayName ?: "greg",
                            rating = data.note,
                            movieId = data.movieId,
                            text = data.comment,
                            isLiked = data.isLiked,
                            id = data.id
                        )
                    )
                    messagesAdapter.notifyDataSetChanged()
                }
            }

            override fun onError(throwable: Throwable) {
                Log.d("Envoi","Ko")
            }

        })
    }

}