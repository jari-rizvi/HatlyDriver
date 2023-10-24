package com.teamx.hatly.ui.fragments.chat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.hatly.data.dataclasses.recievemessage.RecieveMessage
import com.teamx.hatly.databinding.ItemChatRiderBinding
import com.teamx.hatly.databinding.ItemChatUserBinding
import timber.log.Timber

class MessageAdapter(
    private val messageArrayList: ArrayList<RecieveMessage>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_USER = 1
    private val VIEW_TYPE_Rider = 2

    var userId = ""

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_Rider -> {
                MessageGenericViewHolder2(
                    ItemChatRiderBinding.inflate(
                        LayoutInflater.from(viewGroup.context), viewGroup, false
                    ) ,ItemChatUserBinding.inflate(
                        LayoutInflater.from(viewGroup.context), viewGroup, false
                    ),true
                )
            }

            VIEW_TYPE_USER -> {
                MessageGenericViewHolder2(
                    ItemChatRiderBinding.inflate(
                        LayoutInflater.from(viewGroup.context), viewGroup, false
                    ) ,ItemChatUserBinding.inflate(
                        LayoutInflater.from(viewGroup.context), viewGroup, false
                    ),false
                )
            }

            else -> throw IllegalArgumentException("Invalid item type")
        }
    }


    override fun getItemCount(): Int {
        return messageArrayList.size
    }


    override fun getItemViewType(position: Int): Int {
        if (messageArrayList.isNotEmpty()) {
            if (userId.isEmpty()) {
                userId = messageArrayList[0].from
            }
        }
        return if (userId == messageArrayList[position].from) {
            VIEW_TYPE_USER
        } else {
            VIEW_TYPE_Rider
        }

//        return when (messageArrayList[position].isUser) {
//            true -> VIEW_TYPE_USER
//            false -> VIEW_TYPE_Rider
//        }
    }

    private var todayTimeString = ""


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messagesUser = messageArrayList[position]


        holder.itemViewType
        val bool = userId != messageArrayList[position].from
        Log.d("TAG", "onBindViewHolder11111: $bool")
        when (bool) {
            true -> {
                (holder as MessageGenericViewHolder2).bind!!.txtMessage.text = messagesUser.message
                Log.d("TAG", "onBindViewHolder11111: ${messagesUser.message}")
                (holder as MessageGenericViewHolder2).bind!!.todayTime.text = messagesUser.createdAt

//                if (messagesUser.imgMsg.isNullOrBlank()) {
//
//                    (holder as MessageGenericViewHolder2).bind!!.txtMessage.visibility = View.GONE
////                    Picasso.get().load(messagesUser.imgUrl).into(holder.bind!!.imgMessage)
//                } else {
//                    Picasso.get().load(messagesUser.imgMsg).into((holder as MessageGenericViewHolder2).bind!!.txtMessage)
//                    (holder as MessageGenericViewHolder2).bind!!.txtMessage.visibility = View.VISIBLE
//                }

//                if (messagesUser.profileImg.isNullOrBlank()) {
//
//                    (holder as MessageGenericViewHolder2).bind!!.imgUser.visibility = View.GONE
////                    Picasso.get().load(messagesUser.imgUrl).into(holder.bind!!.imgMessage)
//                } else {
//                    Picasso.get().load(messagesUser.profileImg)
//                        .into((holder as MessageGenericViewHolder2).bind!!.imgUser)
//                    (holder as MessageGenericViewHolder2).bind!!.imgUser.visibility = View.VISIBLE
//                }

                (holder as MessageGenericViewHolder2).bind!!.todayTime.text = messagesUser.createdAt

                Timber.tag("TAG").d("onBindViewHolder: true")

//                if (todayTimeString == messagesUser.timeAndDate) {
//                    (holder as MessageGenericViewHolder2).bind!!.todayTime.visibility = View.GONE

//                } else {
//                    (holder as MessageGenericViewHolder2).bind!!.todayTime.visibility = View.VISIBLE

//                }

                todayTimeString = messagesUser.createdAt
            }

            false -> {


                val text: String = messagesUser.message

                Timber.tag("TAG").d("onBindViewHolder: false")

//                (holder as MessageGenericViewHolder2).bind!!.todayTime1.text = messagesUser.timeAndDate

                Timber.tag("TAG")
                    .d("same $text $position ${messagesUser.createdAt} $todayTimeString")

                if (messagesUser.createdAt == todayTimeString) {
                    (holder as MessageGenericViewHolder2).bind1!!.todayTime.visibility = View.GONE
                } else {
                    (holder as MessageGenericViewHolder2).bind1!!.todayTime.visibility = View.VISIBLE
                }


                Log.d("TAG", "onBindViewHolder11111: ${messagesUser.message}")
                (holder as MessageGenericViewHolder2).bind1!!.txtMessage.text = messagesUser.message


                (holder as MessageGenericViewHolder2).bind1!!.todayTime.text = messagesUser.createdAt
//                (holder as MessageGenericViewHolder2).bind1!!.txtMessageTime.text = messagesUser.timeAndDate
//                if (messagesUser.profileImg.isNullOrBlank()) {
//
//                    (holder as MessageGenericViewHolder2).bind1!!.imgUser.visibility = View.GONE
////                    Picasso.get().load(messagesUser.imgUrl).into(holder.bind1!!.imgMessage)
//                } else {
//                    Picasso.get().load(messagesUser.profileImg)
//                        .into((holder as MessageGenericViewHolder2).bind1!!.imgUser)
//                    (holder as MessageGenericViewHolder2).bind1!!.imgUser.visibility = View.VISIBLE
//                }
//                if (messagesUser.imgMsg.isNullOrBlank()) {
//
//                    (holder as MessageGenericViewHolder2).bind1!!.imgMessage.visibility = View.GONE
////                    Picasso.get().load(messagesUser.imgUrl).into(holder.bind1!!.imgMessage)
//                } else {
//                    Picasso.get().load(messagesUser.imgMsg)
//                        .into((holder as MessageGenericViewHolder2).bind1!!.imgMessage)
//                    (holder as MessageGenericViewHolder2).bind1!!.imgMessage.visibility = View.VISIBLE
//                }

                todayTimeString = messagesUser.createdAt
                Timber.tag("TAG").d("working")
            }
//            else -> throw IllegalArgumentException("Invalid item type")
        }

    }
}


class MessageGenericViewHolder2(binding: ItemChatRiderBinding?, binding1: ItemChatUserBinding?, istrue : Boolean) :
    RecyclerView.ViewHolder(if(istrue){
        binding!!.root
    }
    else{
        binding1!!.root
    }) {

    val bind = binding
    val bind1 = binding1

}