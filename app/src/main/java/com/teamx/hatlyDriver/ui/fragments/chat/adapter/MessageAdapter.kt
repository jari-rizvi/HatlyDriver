package com.teamx.hatlyDriver.ui.fragments.chat.adapter

import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.teamx.hatlyDriver.R
import com.teamx.hatlyDriver.data.dataclasses.recievemessage.RecieveMessage
import com.teamx.hatlyDriver.databinding.ItemChatRiderBinding
import com.teamx.hatlyDriver.databinding.ItemChatUserBinding
import com.teamx.hatlyDriver.utils.Helper
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import java.util.regex.Matcher
import java.util.regex.Pattern

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
                    ), ItemChatUserBinding.inflate(
                        LayoutInflater.from(viewGroup.context), viewGroup, false
                    ), true
                )
            }

            VIEW_TYPE_USER -> {
                MessageGenericViewHolder2(
                    ItemChatRiderBinding.inflate(
                        LayoutInflater.from(viewGroup.context), viewGroup, false
                    ), ItemChatUserBinding.inflate(
                        LayoutInflater.from(viewGroup.context), viewGroup, false
                    ), false
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


                (holder as MessageGenericViewHolder2).bind!!.todayTime.text =  getTimeInString(messagesUser.createdAt)

                Timber.tag("TAG").d("onBindViewHolder: true")


                if (Helper.isUrl(messagesUser.message)){
                    (holder as MessageGenericViewHolder2).bind!!.txtMessage.visibility = View.GONE
                    (holder as MessageGenericViewHolder2).bind!!.imgUserChat.visibility = View.VISIBLE
                    Picasso.get().load(messagesUser.message).placeholder(R.drawable.baseline_image_24).error(
                        R.drawable.baseline_image_24).resize(500, 500).into( (holder as MessageGenericViewHolder2).bind!!.imgUserChat)
                }else {
                    (holder as MessageGenericViewHolder2).bind!!.imgUserChat.visibility = View.GONE
                    (holder as MessageGenericViewHolder2).bind!!.txtMessage.visibility = View.VISIBLE
                    (holder as MessageGenericViewHolder2).bind!!.txtMessage.text = try {
                        messagesUser.message
                    } catch (e: Exception) {
                        ""
                    }
                }



                todayTimeString = messagesUser.createdAt
            }

            false -> {

                val text: String = messagesUser.message

                if (messagesUser.createdAt == todayTimeString) {
                    (holder as MessageGenericViewHolder2).bind1!!.todayTime.visibility = View.GONE
                } else {
                    (holder as MessageGenericViewHolder2).bind1!!.todayTime.visibility =
                        View.VISIBLE
                }

                (holder as MessageGenericViewHolder2).bind1!!.txtMessage.text = messagesUser.message
                (holder as MessageGenericViewHolder2).bind1!!.todayTime.text =
                    getTimeInString(messagesUser.createdAt)

                todayTimeString = messagesUser.createdAt

            }
        }

    }
}


class MessageGenericViewHolder2(
    binding: ItemChatRiderBinding?,
    binding1: ItemChatUserBinding?,
    istrue: Boolean
) :
    RecyclerView.ViewHolder(
        if (istrue) {
            binding!!.root
        } else {
            binding1!!.root
        }
    ) {

    val bind = binding
    val bind1 = binding1

}

fun getTimeInString(timestamp: String): String {
    var str = ""

    val pattern: Pattern =
        Pattern.compile("(\\d{4}-\\d{2}-\\d{2})T(\\d{2}:\\d{2}:\\d{2})\\.\\d{3}Z")
    val matcher: Matcher = pattern.matcher(timestamp)
    if (matcher.matches()) {
        val date: String = matcher.group(1)
        val time: String = matcher.group(2)
        println("Date: $date")
        println("Time: $time")
        str = time
    }
    return str
}

 fun timeAgo(timeMili: Long): String {

    return DateUtils.getRelativeTimeSpanString(
        timeMili!!, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS
    ).toString()
}

fun timeFormater(timeMili: String): Long? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var milliseconds: Long? = null
    dateFormat.timeZone = TimeZone.getTimeZone("UTC") // Set the time zone to UTC

    try {
        val date: Date = dateFormat.parse(timeMili)
        return date.time
        println("Timestamp in milliseconds: $milliseconds")
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return milliseconds
}
