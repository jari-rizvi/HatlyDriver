package com.teamx.hatlyUser.ui.fragments.chat

import android.os.Bundle
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.hatly.BR
import com.teamx.hatly.R
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.data.dataclasses.recievemessage.RecieveMessage
import com.teamx.hatly.data.models.riderMessage.RiderMessage
import com.teamx.hatly.databinding.FragmentChatBinding
import com.teamx.hatly.ui.fragments.chat.socket.MessageSocketClass
import com.teamx.hatly.ui.fragments.chat.socket.model.allmessageData.GetAllMessageData
import com.teamx.hatlyUser.ui.fragments.chat.adapter.ChatAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import java.util.regex.Matcher
import java.util.regex.Pattern


@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(), MessageSocketClass.ReceiveSendMessageCallback, MessageSocketClass.GetAllMessageCallBack {

    override val layoutId: Int
        get() = R.layout.fragment_chat
    override val viewModel: Class<ChatViewModel>
        get() = ChatViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    //    lateinit var itemClasses : ArrayList<Boolean>
//    lateinit var layoutManager2 : LinearLayoutManager
//    lateinit var chatAdapter : ChatAdapter
    lateinit var spin_kit: ProgressBar
    private lateinit var options: NavOptions

    private lateinit var messagesUserArrayList: ArrayList<RiderMessage>

    private lateinit var messagesUserAdapter: ChatAdapter

    var userId = ""

    var isScrolling = false
    var currentItems = 0
    var totalItems = 0
    var scrollOutItems = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        options = navOptions {
            anim {
                enter = R.anim.enter_from_left
                exit = R.anim.exit_to_left
                popEnter = R.anim.nav_default_pop_enter_anim
                popExit = R.anim.nav_default_pop_exit_anim
            }
        }


//        MessageSocketClass.connect("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWNhdGlvbiI6eyJpdiI6IjZiNjQ3NTMzNjkzODM3NjM2ODMyNmIzOTM1MzczODY0IiwiZW5jcnlwdGVkRGF0YSI6IjM4OTFhZWVmYjBlZDgwZmU2ZDY3OWEwYWQzY2IzNGQyZWM3MDA4MDFjZWNiZDY0NDk4ZWZlOWEwZjMxMDNkMjEifSwidW5pcXVlSWQiOiI4MDBmYjA4ODFjNGUzYTBiNjdkZmNmMmZhYWRkY2YiLCJpYXQiOjE2OTc0NTQxMzIsImV4cCI6MTAzMzc0NTQxMzJ9.ADKHPgvmRMsAu6EiNZHsLYLAVbhQokpgnhG335SsJ0s","6511befda128e070ad313243")
        MessageSocketClass.connect2(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWNhdGlvbiI6eyJpdiI6IjZiNjQ3NTMzNjkzODM3NjM2ODMyNmIzOTM1MzczODY0IiwiZW5jcnlwdGVkRGF0YSI6IjM4OTFhZWVmYjBlZDgwZmU2ZDY3OWEwYWQzY2IzNGQyZWM3MDA4MDFjZWNiZDY0NDk4ZWZlOWEwZjMxMDNkMjEifSwidW5pcXVlSWQiOiI0OGZiMTU2OTg2ZDNkM2IzYmQ3ZTIyMjM0MmY0YTQiLCJpYXQiOjE2OTc0NzA4MzksImV4cCI6MTAzMzc0NzA4Mzl9.V-hG2OFgmRy8D0PQCICXNHp6GeqUpAXq09hqU8OXeco",
            "6511befda128e070ad313243"
        ,this,this)
        mViewDataBinding.imgSend.setOnClickListener {
            val text: String = mViewDataBinding.inpChat.text.toString()
            if (text.isNotEmpty()) {
                MessageSocketClass.sendMessageTo(text, this)
            } else if (text.isNotBlank()) {
                MessageSocketClass.sendMessageTo(text, this)
            } else if (text.isBlank()) {
//                MessageSocketClass.sendMessageTo(strImg, " $text ", this)

            }
//            mViewDataBinding.imgToSend.visibility = View.INVISIBLE
//            strImg = ""
            mViewDataBinding.inpChat.setText("")
        }


        MessageSocketClass.getAllMessage(5,5, this)


//        val recChat = view.findViewById<RecyclerView>(R.id.recChat)

//        spin_kit = view.findViewById(R.id.spin_kit)
//
//        var layoutManager2 = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
//
//        recChat.layoutManager = layoutManager2
//
//        var itemClasses : ArrayList<Boolean> = ArrayList()
        /*
        itemClasses.add(true)
        itemClasses.add(false)
        itemClasses.add(true)
        itemClasses.add(false)
        itemClasses.add(true)
        itemClasses.add(false)
        itemClasses.add(true)
        itemClasses.add(false)
        itemClasses.add(true)
        itemClasses.add(false)
        itemClasses.add(false)
        itemClasses.add(true)
        itemClasses.add(true)
        itemClasses.add(true)
        itemClasses.add(false)
        itemClasses.add(false)
        itemClasses.add(false)
        itemClasses.add(false)
        itemClasses.add(false)
        itemClasses.add(true)
        itemClasses.add(true)
        itemClasses.add(true)
        itemClasses.add(false)
        itemClasses.add(true)
        itemClasses.add(true)*/

//        var chatAdapter = ChatAdapter(itemClasses)
//        recChat.adapter = chatAdapter
        initializeAdapter()
    }


    private fun initializeAdapter() {
        /*     GlobalScope.launch(Dispatchers.Main) {
            delay(1500)
            mViewDataBinding.shimmerLayout.visibility = View.GONE
            mViewDataBinding.recyclerView.visibility = View.VISIBLE
        }*/
        messagesUserArrayList = ArrayList()
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.recChat.layoutManager = linearLayoutManager
        messagesUserAdapter = ChatAdapter(messagesUserArrayList)
        mViewDataBinding.recChat.adapter = messagesUserAdapter

    }

    private fun timeAgo(timeMili: Long): String {

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

    var strImg = ""

    override fun onGetReceiveMessage(getAllChatsData: RecieveMessage) {

        Timber.tag("MessageUserFragment").d("onGetReceiveMessage${getAllChatsData.message}: ")
        Timber.tag("MessageUserFragment").d("onGetReceiveMessage${getAllChatsData}: ")

        var timestamp = ""

        timestamp = getAllChatsData.createdAt ?: ""

        val dateAndTime = timeAgo(timeFormater(getAllChatsData.createdAt ?: "")!!)


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

        GlobalScope.launch(Dispatchers.Main) {
            messagesUserArrayList.add(
                RiderMessage("2", "", "", "", "", false, "fdfdf", false, "", "", "")
            )
            Log.d("TAG", "messageaaya: ${messagesUserArrayList.size}")
            messagesUserAdapter.notifyDataSetChanged()
            mViewDataBinding.recChat.scrollToPosition(messagesUserArrayList.size - 1)

        }
    }

        override fun responseMessage2(str: String) {
            Timber.tag("MessageUserFragment").d("responseMessage2: ")

            GlobalScope.launch(Dispatchers.Main) {

                var timestamp = ""
                timestamp = "it.createdAt"
                var str2 = ""
                val pattern: Pattern =
                    Pattern.compile("(\\d{4}-\\d{2}-\\d{2})T(\\d{2}:\\d{2}:\\d{2})\\.\\d{3}Z")
                val matcher: Matcher = pattern.matcher(timestamp)
                if (matcher.matches()) {
                    val date: String = matcher.group(1)
                    val time: String = matcher.group(2)
                    println("Date: $date")
                    println("Time: $time")
                    str2 = time
                }
                messagesUserArrayList.add(
                    RiderMessage("2", "", "", "", "", false, "fdfddfd", false, "", "", "")
                )
                Log.d("TAG", "messageaaya: $str")
                messagesUserAdapter.notifyDataSetChanged()
                mViewDataBinding.recChat.scrollToPosition(messagesUserArrayList.size - 1)
            }
        }

    override fun onGetAllMessage(getAllChatsData: GetAllMessageData) {
        Timber.tag("MessageUserFragment").d("onGetAllMessage: ${getAllChatsData.docs.size}")
        GlobalScope.launch(Dispatchers.Main) {
            Timber.tag("MessageUserFragment").d("Dispatchers: ${getAllChatsData.docs.size}")

            messagesUserArrayList.clear()


            getAllChatsData.docs.forEachIndexed { i, it ->


                var timestamp = ""

                timestamp = it.createdAt

                val dateAndTime = timeAgo(timeFormater(it.createdAt)!!)

                var timestampString = ""

                val calendar: Calendar = Calendar.getInstance()
                calendar.timeInMillis =
                    timeFormater(it.createdAt)!! // pass the timestamp value in milliseconds

                timestampString = if (DateUtils.isToday(timeFormater(it.createdAt)!!)) {
                    "Today " /*+ DateFormat.format("h:mm a", calendar).toString();*/
                } else if (DateUtils.isToday(timeFormater(it.createdAt)!! + DateUtils.DAY_IN_MILLIS)) {
                    "Yesterday " /*+ DateFormat.format("h:mm a", calendar).toString();*/
                } else {
                    //                    timestampString = DateFormat.format("MMM dd, yyyy h:mm a", calendar).toString();
                    DateFormat.format("MMM dd, yyyy", calendar).toString();
                }


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

                Timber.tag("MessageUserFragment").d("timeFormatter: ${it.message}")

                if (userId == it.from) {
                    messagesUserArrayList.add(
                            RiderMessage("2", "", "", "", "", false, "fdfddfd", true, "", "", "")
                    )
                } else {
                    messagesUserArrayList.add(
                        RiderMessage("1", "", "", "", "", false, "fdfddfd", true, "", "", "")

                    )
                }

            }

            messagesUserAdapter.notifyDataSetChanged()
        }
    }

    override fun responseMessage() {
        Timber.tag("MessageUserFragment").d("responseMessage: ")
    }

//    override fun responseMessage2(str: String, strImg: String) {
//        Timber.tag("MessageUserFragment").d("responseMessage2: ")
//        GlobalScope.launch(Dispatchers.Main) {
//
//            var timestamp = ""
//
//            timestamp = "it.createdAt"
//            val pattern: Pattern =
//                Pattern.compile("(\\d{4}-\\d{2}-\\d{2})T(\\d{2}:\\d{2}:\\d{2})\\.\\d{3}Z")
//            val matcher: Matcher = pattern.matcher(timestamp)
//            if (matcher.matches()) {
//                val date: String = matcher.group(1)
//                val time: String = matcher.group(2)
//                println("Date: $date")
//                println("Time: $time")
////                str2 = time
//            }
//            messagesUserArrayList.add(
//                RiderMessage("2",timeAgo(System.currentTimeMillis()),"","","",false,str,false,"","",""))
//            messagesUserAdapter.notifyDataSetChanged()
//            mViewDataBinding.recChat.scrollToPosition(messagesUserArrayList.size - 1)
//        }
//    }



}