package com.master.movieadvisor.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.master.movieadvisor.ListMessagesAdapter
import com.master.movieadvisor.R
import com.master.movieadvisor.helpers.SwipeToDeleteCallback
import com.master.movieadvisor.model.Comment
import com.master.movieadvisor.model.MessagesViewModel
import com.master.movieadvisor.service.providers.NetworkListener
import com.master.movieadvisor.service.providers.NetworkProvider
import com.master.movieadvisor.ui.toEditable
import kotlinx.android.synthetic.main.fragment_message.*

class HistoryMessageFragment : Fragment() {
    private val messagesAdapter by lazy { ListMessagesAdapter() }
    private lateinit var commentView: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

    }

    override fun onResume() {
        super.onResume()
        auth = Firebase.auth
        currentUser = auth.currentUser
        currentUser?.let { it ->
            NetworkProvider.getOpinionsByUser(idUser = "15e7frge3943e4geg48",listener = object:
                NetworkListener<List<Comment>> {
                override fun onSuccess(data: List<Comment>) {
                    val transformData=data.map {comment->
                        MessagesViewModel(userName = "greg",rating = comment.rating,movieId = comment.movieId,text = comment.comment,isLiked = comment.like)
                    }
                    messagesAdapter.listItem=transformData as MutableList<MessagesViewModel>
                }

                override fun onError(throwable: Throwable) {
                    Log.e("Error", throwable.localizedMessage)
                }

            })        }

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recyclerview_message, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listMessageView.apply {
            adapter =messagesAdapter
        }
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(view.context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                messagesAdapter.listItem.removeAt(pos)
                messagesAdapter.notifyItemRemoved(pos)
            }
        }
        messagesAdapter.messageListener = { updateMessage(it) }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(listMessageView)


    }

    private fun updateMessage(message: MessagesViewModel) {
        val builder = AlertDialog.Builder(activity,R.style.AlertDialogTheme)
        // Get the layout inflater
        val view: View = LayoutInflater.from(activity).inflate(R.layout.dialog_avis, null)
        commentView = view.findViewById(R.id.comment)
        ratingBar = view.findViewById(R.id.eval_user)
        commentView.text = message.text.toEditable()
        ratingBar.rating=message.rating.toFloat()

        builder.setView(view)
            .setTitle(getString(R.string.title_dialog_rating))
            // Add action button
            .setPositiveButton(R.string.send)
            { _, _ ->
                val comment = commentView.text.toString()
                val rating = ratingBar.rating.toDouble()
                //applyRating(rating, comment)

            }
            .setNegativeButton(R.string.cancel)
            { _, _ ->

            }

        builder.create().show()

    }
}