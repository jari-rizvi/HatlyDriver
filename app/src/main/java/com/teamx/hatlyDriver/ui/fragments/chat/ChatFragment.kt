package com.teamx.hatlyDriver.ui.fragments.chat

import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.hatlyDriver.BR
import com.teamx.hatlyDriver.MainApplication
import com.teamx.hatlyDriver.R
import com.teamx.hatlyDriver.baseclasses.BaseFragment
import com.teamx.hatlyDriver.constants.NetworkCallPoints
import com.teamx.hatlyDriver.databinding.FragmentChatBinding
import com.teamx.hatlyDriver.localization.LocaleManager
import com.teamx.hatlyDriver.ui.fragments.chat.adapter.MessageAdapter
import com.teamx.hatlyDriver.ui.fragments.chat.socket.MessageSocketClass
import com.teamx.hatlyDriver.ui.fragments.chat.socket.model.allmessageData.Doc
import com.teamx.hatlyDriver.ui.fragments.chat.socket.model.allmessageData.GetAllMessageData
import com.teamx.hatlyDriver.utils.PrefHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
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

    private lateinit var messagesUserArrayList: ArrayList<Doc>

    private lateinit var messagesUserAdapter: MessageAdapter
    private lateinit var recChat: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var inpChat: EditText
    lateinit var handler: Handler
    lateinit var runnable: Runnable



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
        handler = Handler()

        recChat = view.findViewById(R.id.recChat)
        linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recChat.layoutManager = linearLayoutManager
        inpChat = view.findViewById(R.id.inpChat)


        val userData = PrefHelper.getInstance(requireActivity()).getUserData()

        if (userData != null) {
            userId = userData._id
        }



        if (!MainApplication.localeManager!!.getLanguage()
                .equals(LocaleManager.Companion.LANGUAGE_ENGLISH)
        ) {

            mViewDataBinding.imgBack.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                       R.drawable.stripe_ic_arrow_right_circle,
                    requireActivity().theme
                )
            )

        } else {
            mViewDataBinding.imgBack.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.back_arrow,
                    requireActivity().theme
                )
            )

        }

        mViewDataBinding.imgBack.setOnClickListener {
            MessageSocketClass.disconnect()
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.trackFragment, arguments, options)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    MessageSocketClass.disconnect()
                    navController =
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    navController.navigate(R.id.trackFragment, arguments, options)

                }
            })

        val bundle = arguments
        orderId = bundle?.getString("orderId").toString()


//        runnable = Runnable {
//
//        }
//
//        handler.postDelayed(runnable, 3000)
//


        GlobalScope.launch(Dispatchers.IO) {
            MessageSocketClass.connect2(
                NetworkCallPoints.TOKENER, orderId, this@ChatFragment, this@ChatFragment
            )
        }




//        MessageSocketClass.connect2(
//            NetworkCallPoints.TOKENER, "6511befda128e070ad313243", this, this
//        )

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


//        MessageSocketClass.getAllMessage(1000, 5, this)


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
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        try {
            val date: Date = dateFormat.parse(timeMili)
            return date.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return milliseconds
    }

    override fun onGetReceiveMessage(getAllChatsData: Doc) {

       /* var timestamp = ""

        timestamp = getAllChatsData.createdAt ?: ""*/

    /*    val dateAndTime = timeAgo(timeFormater(getAllChatsData.createdAt ?: "")!!)

        val str = getTimeInString(timestamp)*/

        GlobalScope.launch(Dispatchers.Main) {
            messagesUserArrayList.add(
                getAllChatsData
            )

            messagesUserAdapter.notifyDataSetChanged()
            mViewDataBinding.recChat.scrollToPosition(messagesUserArrayList.size - 1)

        }
    }

    override fun responseMessage2(str: String) {
        Log.d("onGetAllMessage", "responseMessage2 $str")

//        GlobalScope.launch(Dispatchers.Main) {
//
//            var timestamp = ""
//            timestamp = "it.createdAt"
//            val str2 = getTimeInString(timestamp)
//            messagesUserArrayList.add(
//                RecieveMessage("2", str2, "", "", "", false, str, "false", "", "")
//            )
//            messagesUserAdapter.notifyDataSetChanged()
//            mViewDataBinding.recChat.scrollToPosition(messagesUserArrayList.size - 1)
//        }
    }

//    override fun onGetAllMessage(getAllMessageData: Doc) {
////        GlobalScope.launch(Dispatchers.Main) {
////            Log.d("TAG", "onGetAllMessage: ${getAllMessageData}")
////            messagesUserArrayList.clear()
////
////            getAllMessageData.forEachIndexed { i, it ->
////
////
////                var timestamp = ""
////
////                timestamp = it.createdAt
////
////                val dateAndTime = timeAgo(timeFormater(it.createdAt)!!)
////
////                var timestampString = ""
////
////                val calendar: Calendar = Calendar.getInstance()
////                calendar.timeInMillis =
////                    timeFormater(it.createdAt)!! // pass the timestamp value in milliseconds
////
////                timestampString = if (DateUtils.isToday(timeFormater(it.createdAt)!!)) {
////                    "Today " /*+ DateFormat.format("h:mm a", calendar).toString();*/
////                } else if (DateUtils.isToday(timeFormater(it.createdAt)!! + DateUtils.DAY_IN_MILLIS)) {
////                    "Yesterday " /*+ DateFormat.format("h:mm a", calendar).toString();*/
////                } else {
////                    //                    timestampString = DateFormat.format("MMM dd, yyyy h:mm a", calendar).toString();
////                    DateFormat.format("MMM dd, yyyy", calendar).toString();
////                }
////
////
////                val str = getTimeInString(timestamp)
////
////                if (userId.isEmpty()) {
////                    userId = it.from
////                }
////            }
////
////            messagesUserAdapter.notifyDataSetChanged()
////        }
//
//
//        GlobalScope.launch(Dispatchers.Main) {
//
//
//            getAllMessageData.isDriver = getAllMessageData.from == userId
//            messagesUserArrayList.add(getAllMessageData)
////        chatAdapter.notifyItemInserted(chatArrayList.size+1)
//            inpChat.setText("")
//            recChat.adapter?.notifyItemInserted(messagesUserArrayList.size + 1)
//            val lastItemPosition = messagesUserAdapter.itemCount - 1
//            linearLayoutManager.scrollToPosition(lastItemPosition)
//        }
//
//    }


    override fun onGetAllMessage(getAllChatsData: GetAllMessageData) {
        GlobalScope.launch(Dispatchers.Main) {
//            delay(2000)
            Log.d("onGetAllMessage", "onStateChanged: click $getAllChatsData")
            messagesUserArrayList.clear()


            getAllChatsData.docs.forEach {
                it.isDriver = it.from == userId
                messagesUserArrayList.add(it)
//                chatAdapter.messageArrayList.add(it)

            }

            Log.d("chatArrayListS", "onGetAllMessage: ${messagesUserArrayList.size}")

            val lastItemPosition = messagesUserAdapter.itemCount - 1
            linearLayoutManager.scrollToPosition(lastItemPosition)
            messagesUserAdapter.notifyDataSetChanged()
        }

    }


    override fun responseMessage() {
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