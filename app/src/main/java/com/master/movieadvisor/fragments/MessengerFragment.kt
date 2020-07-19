package com.master.movieadvisor.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.master.movieadvisor.model.MessagesViewModel
import com.master.movieadvisor.ListMessagesAdapter
import com.master.movieadvisor.R
import com.master.movieadvisor.model.Comment
import com.master.movieadvisor.service.providers.NetworkListener
import com.master.movieadvisor.service.providers.NetworkProvider
import kotlinx.android.synthetic.main.fragment_message.*



class MessengerFragment : Fragment(){
    private var movieId: Int? = null
    private val messagesAdapter by lazy { ListMessagesAdapter() }
    private lateinit var buttonComment: Button
    private var enableStatusChangeListener: Boolean?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true


    }

    override fun onResume() {
        super.onResume()


    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments:Bundle? = arguments
        /*buttonComment = view.findViewById(R.id.btn_create_opinion)
        buttonComment.isEnabled=enableStatusChangeListener?:false*/

        movieId=arguments?.getInt("movieId")
        listMessageView.adapter =messagesAdapter


    }


}
