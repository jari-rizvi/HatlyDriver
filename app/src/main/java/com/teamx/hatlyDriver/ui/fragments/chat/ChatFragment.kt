package com.teamx.hatlyDriver.ui.fragments.chat

import android.os.Bundle
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.hatlyDriver.BR
import com.teamx.hatlyDriver.R
import com.teamx.hatlyDriver.baseclasses.BaseFragment
import com.teamx.hatlyDriver.constants.NetworkCallPoints
import com.teamx.hatlyDriver.data.dataclasses.recievemessage.RecieveMessage
import com.teamx.hatlyDriver.databinding.FragmentChatBinding
import com.teamx.hatlyDriver.ui.fragments.chat.adapter.MessageAdapter
import com.teamx.hatlyDriver.ui.fragments.chat.socket.MessageSocketClass
import com.teamx.hatlyDriver.ui.fragments.chat.socket.model.allmessageData.GetAllMessageData
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
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(),
    MessageSocketClass.ReceiveSendMessageCallback, MessageSocketClass.GetAllMessageCallBack {

    override val layoutId: Int
        get() = R.layout.fragment_chat
    override val viewModel: Class<ChatViewModel>
        get() = ChatViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    //    private lateinit var messagesUserArrayList: ArrayList<RiderMessage>
    private lateinit var messagesUserArrayList: ArrayList<RecieveMessage>

    //    private lateinit var messagesUserAdapter: ChatAdapter
    private lateinit var messagesUserAdapter: MessageAdapter

    var userId = ""
    private var orderId: String = ""


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
        mViewDataBinding.imgBack.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.homeFragment, arguments, options)
            MessageSocketClass.disconnect()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Handle the back button event here
                    navController =
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    navController.navigate(R.id.homeFragment, arguments, options)
                    MessageSocketClass.disconnect()


                }
            })

        val bundle = arguments
        orderId = bundle?.getString("orderId").toString()

        Log.d("TAG", "orderIdorderIdorderId: $orderId")


//        MessageSocketClass.connect("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWNhdGlvbiI6eyJpdiI6IjZiNjQ3NTMzNjkzODM3NjM2ODMyNmIzOTM1MzczODY0IiwiZW5jcnlwdGVkRGF0YSI6IjM4OTFhZWVmYjBlZDgwZmU2ZDY3OWEwYWQzY2IzNGQyZWM3MDA4MDFjZWNiZDY0NDk4ZWZlOWEwZjMxMDNkMjEifSwidW5pcXVlSWQiOiI4MDBmYjA4ODFjNGUzYTBiNjdkZmNmMmZhYWRkY2YiLCJpYXQiOjE2OTc0NTQxMzIsImV4cCI6MTAzMzc0NTQxMzJ9.ADKHPgvmRMsAu6EiNZHsLYLAVbhQokpgnhG335SsJ0s","6511befda128e070ad313243")
        MessageSocketClass.connect2(
            NetworkCallPoints.TOKENER, orderId, this, this
        )
        mViewDataBinding.imgSend.setOnClickListener {
            val text: String = mViewDataBinding.inpChat.text.toString()
            if (text.isNotEmpty()) {
                MessageSocketClass.sendMessageTo(text, this)
            } else if (text.isNotBlank()) {
                MessageSocketClass.sendMessageTo(text, this)
            } else if (text.isBlank()) {
//                MessageSocketClass.sendMessageTo(strImg, " $text ", this)

            }

            mViewDataBinding.inpChat.setText("")
        }


        MessageSocketClass.getAllMessage(5, 5, this)


        initializeAdapter()
    }


    private fun initializeAdapter() {

        messagesUserArrayList = ArrayList()
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.recChat.layoutManager = linearLayoutManager
        messagesUserAdapter = MessageAdapter(messagesUserArrayList)
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

        val str = getTimeInString(timestamp)

        GlobalScope.launch(Dispatchers.Main) {
            messagesUserArrayList.add(
                getAllChatsData
//                RiderMessage(
//                    "2",
//                    str,
//                    "",
//                    "",
//                    "",
//                    false,
//                    getAllChatsData.message,
//                    true,
//                    "",
//                    "",
//                    ""
//                )
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
            val str2 = getTimeInString(timestamp)
            messagesUserArrayList.add(
                RecieveMessage("2", str2, "", "", "", false, str, "false", "", "")
            )
            Log.d("TAG", "messageaaya: $str")
            messagesUserAdapter.notifyDataSetChanged()
            mViewDataBinding.recChat.scrollToPosition(messagesUserArrayList.size - 1)
        }
    }

    override fun onGetAllMessage(getAllMessageData: GetAllMessageData) {
        Timber.tag("MessageUserFragment").d("onGetAllMessage: ${getAllMessageData.docs.size}")
        GlobalScope.launch(Dispatchers.Main) {
            Timber.tag("MessageUserFragment").d("Dispatchers: ${getAllMessageData.docs.size}")

            messagesUserArrayList.clear()


            getAllMessageData.docs.forEachIndexed { i, it ->


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


                val str = getTimeInString(timestamp)

                Timber.tag("MessageUserFragment").d("timeFormatter: ${it.message}")
                if (userId.isEmpty()) {
                    userId = it.from
                }
//                if (userId == it.from) {
//                    messagesUserArrayList.add(
////                        RiderMessage("2", str, "", "", "", false, it.message, true, "", "", "")
//                    )
//                } else {
//                    messagesUserArrayList.add(
////                        RiderMessage("1", str, "", "", "", false, it.message, true, "", "", "")
//
//                    )
//                }
//                userId = it.from

            }

            messagesUserAdapter.notifyDataSetChanged()
        }
    }

    override fun responseMessage() {
        Timber.tag("MessageUserFragment").d("responseMessage: ")
    }


    private fun getTimeInString(timestamp: String): String {
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

}