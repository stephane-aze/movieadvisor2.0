package com.master.movieadvisor

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.master.movieadvisor.model.MessagesViewModel


class ListMessagesAdapter: RecyclerView.Adapter<MessagesViewHolder>() {
    var messageListener: ((MessagesViewModel) -> Unit)? = null
    var listItem: MutableList<MessagesViewModel> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val message: MessagesViewModel = listItem[position]
        holder.itemView.setOnLongClickListener {
            messageListener?.invoke(message)
            return@setOnLongClickListener true
        }
        holder.bind(message)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessagesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MessagesViewHolder(inflater,parent)
    }
    override fun getItemCount(): Int= listItem.size


}
class MessagesViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_message, parent, false)){
    private var usernameView: TextView = itemView.findViewById(R.id.username_message)
    private var userImgView: ImageView = itemView.findViewById(R.id.user_img_message)
    private var signLike: ImageView = itemView.findViewById(R.id.signLike)
    private var signDislike: ImageView = itemView.findViewById(R.id.signDislike)
    private var messageView: TextView = itemView.findViewById(R.id.text_message)
    private var ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar2)


    fun bind(message: MessagesViewModel) {
        usernameView.text = message.userName
        messageView.text = message.text
        ratingBar.rating = message.rating.toFloat()
        Glide.with(itemView)
            .load(message.userImg)
            .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
            .into(userImgView)
        when(message.isLiked){

            true->{signDislike.visibility=View.INVISIBLE
                signLike.visibility=View.VISIBLE


            }
            else->{signDislike.visibility=View.VISIBLE
                signLike.visibility=View.INVISIBLE

            }
        }
    }

}