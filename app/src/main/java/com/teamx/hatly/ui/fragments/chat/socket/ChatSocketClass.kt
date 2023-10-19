package com.teamx.raseef.ui.fragments.messages.socket

import com.google.gson.Gson
import com.teamx.hatly.constants.AppConstants.ApiConfiguration.Companion.BASE_URL
import com.teamx.hatly.data.models.socket_models.ExceptionData
import com.teamx.hatly.data.models.socket_models.HelloData
import com.teamx.hatly.ui.fragments.chat.socket.MessageSocketClass
import com.teamx.hatly.ui.fragments.chat.socket.MessageSocketClass.initiateChat
import com.teamx.raseef.ui.fragments.messages.socket.model.GetAllChatsModelX
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject
import timber.log.Timber

object ChatSocketClass {
    var chatsSocket: Socket? = null
    val gson = Gson()
    fun connect(
        token: String,orderId: String/*, userId: String, allChatsCallBack: AllChatsCallBack
    */) {
        var tokenss = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoidXNhbWE1QHlvcG1haWwuY29tIiwicm9sZXMiOlsiY3VzdG9tZXIiXSwiaWF0IjoxNjc2NDc4MzI3LCJleHAiOjE2NzcwODMxMjd9.QIwLIDy-VaN4_-JmbVKankPcihs2iuZZTOsLRLkPJPw"
        val options = IO.Options()
        val headers: MutableMap<String, String> = HashMap()
        headers["token"] = "$token"

        Timber.tag("ChatSocketClass").d("EVENT_CONNECT: $token")
        options.auth = headers
        options.transports = arrayOf(WebSocket.NAME, "chatting")
        val u: Boolean? = chatsSocket?.connected()
        if (u == null) {

            chatsSocket = IO.socket(

                "$BASE_URL/chat", options
            )
            chatsSocket?.connect()
        } else if (!u) {
            chatsSocket = IO.socket(

                "$BASE_URL/chat", options
            )
            chatsSocket?.connect()
        }

        chatsSocket?.on(Socket.EVENT_CONNECT) {
            Timber.tag("ChatSocketClass").d("EVENT_CONNECT: ${MessageSocketClass.userMessageSocket?.connected()}")
            initiateChat(orderId = "6511befda128e070ad313243")
//            emitUserForAllChat(userId)
        }
        chatsSocket?.on(Socket.EVENT_CONNECT_ERROR) {
            Timber.tag("ChatSocketClass").d("EVENT_CONNECT_ERROR: ${it[0]}")
        }

        chatsSocket?.on("HELLO") { args ->
            val HELLO = gson.fromJson(args[0].toString(), HelloData::class.java)
            Timber.tag("ChatSocketClass").d("HELLO: ${HELLO.order}")

        }
        chatsSocket?.on("exception") { args ->
            try {
                val exception = gson.fromJson(args[0].toString(), ExceptionData::class.java)
                Timber.tag("ChatSocketClass").d("ExceptionData: ${exception.message}")
            } catch (e: java.lang.Exception) {
                Timber.tag("ChatSocketClass").d("ExceptionData: ${args[0]}")
            }
        }
//        chatsSocket?.on("Get_ALL_CHATS") { args ->
//            try {
//                Timber.tag("ChatSocketClass").d("Get_ALL_CHATS: ${args.get(0)}")
//                val exception = gson.fromJson(args[0].toString(), GetAllChatsModelX::class.java)
//                allChatsCallBack.onAllChatsReceived(exception)
//            } catch (e: java.lang.Exception) {
//                Timber.tag("ChatSocketClass").d("ExceptionData: ${args[0]}")
//            }
//        }

//       getRoomDetail(callback)
//       receivedMessageOn(callback1)
//       getAllMessagesListener(callback)
//       getHellow()
    }

    fun emitUserForAllChat(userId: String) {
        val data = JSONObject().put("user_id", "$userId")
        Timber.tag("ChatSocketClass").d("emitUserForAllChat:$data ")
        chatsSocket?.emit("ALL_CHATS", data, object : Ack {
            override fun call(vararg args: Any?) {
                Timber.tag("ChatSocketClass").d("initiatedChat: ")
            }
        })
    }

    fun disconnect() {
        chatsSocket?.disconnect()
        chatsSocket?.on(Socket.EVENT_DISCONNECT) {
            Timber.tag("ChatSocketClass")
                .d("EVENT_DISCONNECT: ${MessageSocketClass.userMessageSocket?.connected()}")
        }
    }
}

interface AllChatsCallBack {
    fun onAllChatsReceived(getAllChatsModelX: GetAllChatsModelX)
}