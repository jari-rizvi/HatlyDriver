package com.teamx.hatlyDriver.ui.fragments.chat.socket

import android.util.Log
import com.google.gson.Gson
import com.teamx.hatlyDriver.constants.AppConstants.ApiConfiguration.Companion.BASE_URL_CHAT
import com.teamx.hatlyDriver.data.models.socket_models.ExceptionData
import com.teamx.hatlyDriver.ui.fragments.chat.socket.model.allmessageData.Doc
import com.teamx.hatlyDriver.ui.fragments.chat.socket.model.allmessageData.GetAllMessageData
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import timber.log.Timber

object MessageSocketClass {

    fun connect2(
        token: String,
        orderId: String,
        callback1: ReceiveSendMessageCallback,
        callback: GetAllMessageCallBack
    ) {

        val options = IO.Options()
        val headers: MutableMap<String, List<String>> = HashMap()


        headers["Authorization"] = listOf(token)

        Timber.tag("MessageSocketClass").d("EVENT_CONNECT1212: ${userMessageSocket?.connected()}")
        Timber.tag("MessageSocketClass").d("EVENT_CONNECT1212: ")
        options.extraHeaders = headers


        val u: Boolean? = userMessageSocket?.connected()

        if (u == null) {
            userMessageSocket = IO.socket("$BASE_URL_CHAT/chat", options)
        } else if (!u) {
            userMessageSocket = IO.socket("$BASE_URL_CHAT/chat", options)


        }
        if (!userMessageSocket!!.connected()) {

            userMessageSocket?.connect()
        }

        Timber.tag("MessageSocketClass").d("EVENT_CONNECT: ${userMessageSocket?.connected()}")
        Log.d("MessageSocketClass", "EVENT_CONNECT: ${userMessageSocket?.connected()}")

        userMessageSocket?.on(Socket.EVENT_CONNECT) {
            onListenerEverything(callback, callback1)
            Timber.tag("MessageSocketClass").d("EVENT_CONNECT1: ${userMessageSocket?.connected()}")
            initiateChat(orderId = orderId)
            getAllMessage(1000, 5, callback)


//            userMessageSocket?.disconnect()


            Timber.tag("MessageSocketClass").d("EVENT_CONNECT12: ${userMessageSocket?.connected()}")

            sendMessageTo(callback1)

            getAllMessagesListener(callback)

        }
        userMessageSocket?.on(Socket.EVENT_CONNECT_ERROR) {
            Timber.tag("MessageSocketClass")
                .d("EVENT_CONNECT_ERROR22:${userMessageSocket?.connected()} ${it[0]}")

        }

    }

    val gson = Gson()


//    private fun sendMessageTo(callback: ReceiveSendMessageCallback) {
//        userMessageSocket?.on("RECIEVED_MESSAGE") { args ->
//            try {
//                val receiveMessage = gson.fromJson(args[0].toString(), ReceiveMessage::class.java)
//                Timber.tag("MessageSocketClass").d(
//                    "RECIEVED_MESSAGE: ${args[0].toString()} message12312312321 ${receiveMessage.content}"
//                )
//                Timber.tag("MessageSocketClass").d(
//                    "RECIEVED_MESSAGE: ${receiveMessage._id} message ${receiveMessage.media}"
//                )
//                callback.onGetReceiveMessage(receiveMessage)
//            } catch (e: java.lang.Exception) {
//                Timber.tag("MessageSocketClass").d("RECIEVED_MESSAGE: ${args[0]}")
//            }
//        }
//
//    }

    private fun onListenerEverything(
        callback: GetAllMessageCallBack,
        callback2: ReceiveSendMessageCallback
    ) {
        userMessageSocket?.on("exception") { args ->

            try {
                val exception = gson.fromJson(args[0].toString(), ExceptionData::class.java)
                Timber.tag("MessageSocketClass").d("ExceptionData: ${exception.message}")
            } catch (e: java.lang.Exception) {
                Timber.tag("MessageSocketClass").d("ExceptionData: ${args[0]}")

            }

        }
        userMessageSocket?.on("CHAT_INICIATED") { args ->

            Timber.tag("MessageSocketClass").d("ExceptionData2: }${args.get(0)}")

//            sendMessageTo("${args.get(0)}")

        }
        userMessageSocket?.on("RECEIVE_MESSAGE") { args ->
            Timber.tag("MessageSocketClass").d("RECEIVE_MESSAGE: }${args.get(0)}")
            try {
                val receiveMessage = gson.fromJson(args[0].toString(), Doc::class.java)
                Timber.tag("MessageSocketClass").d(
                    "RECIEVED_MESSAGE1212: ${args[0].toString()} message12312312321 ${receiveMessage.message}"
                )
                callback2.onGetReceiveMessage(receiveMessage)
            } catch (e: java.lang.Exception) {
                Timber.tag("MessageSocketClass").d("RECIEVED_MESSAGE: ${args[0]}")
            }

        }
        userMessageSocket?.on("CHAT_HISTORY") { args ->
            Timber.tag("MessageSocketClass").d("CHAT_HISTORY: }${args.get(0)}")

        }
        userMessageSocket?.on("CHAT_LEVED") { args ->
            Timber.tag("MessageSocketClass").d("CHAT_LEVED: }${args.get(0)}")

        }
        userMessageSocket?.on("ALL_CHATS_READED") { args ->
            Timber.tag("MessageSocketClass").d("ALL_CHATS_READED: }${args.get(0)}")

        }
        userMessageSocket?.on("READED_SUCCESSFULLY") { args ->
            Timber.tag("MessageSocketClass").d("READED_SUCCESSFULLY: }${args.get(0)}")

        }
    }


    private fun getAllMessagesListener(callback: GetAllMessageCallBack) {
        userMessageSocket?.on("CHAT_HISTORY") { args ->
            try {
                val exception = gson.fromJson(args[0].toString(), GetAllMessageData::class.java)
                Timber.tag("MessageSocketClass").d("Get_ALL_MESSAGES: ${exception.docs.size}")
                callback.onGetAllMessage(exception)
            } catch (e: Exception) {
                Timber.tag("MessageSocketClass").d("Get_ALL_MESSAGES: ${args[0]}")
            }
        }
    }


    /*  private fun getAllChats(callback: GetAllChatCallBack) {
          userMessageSocket?.on("All_CHATS") { args ->
              try {
                  val allChatsData = gson.fromJson(args[0].toString(), GetAllChatsData::class.java)
                  Timber.tag("MessageSocketClass").d("All_CHATS: ${allChatsData.size}")
                  callback.responseCHATALL(allChatsData)
              } catch (e: java.lang.Exception) {
                  Timber.tag("MessageSocketClass").d("All_CHATS: ${args[0]}")
              }
          }
      }

      private fun getAllChats2(callback: GetAllChatCallBack) {
          userMessageSocket?.on("Get_ALL_CHATS") { args ->
              try {
                  val allChatsData = gson.fromJson(args[0].toString(), GetAllChatsData::class.java)
                  callback.getAllChatsModel(allChatsData)
                  Timber.tag("MessageSocketClass").d("Get_ALL_CHATS: ${allChatsData.size}")
              } catch (e: java.lang.Exception) {
                  Timber.tag("MessageSocketClass").d("Get_ALL_CHATS: ${args[0]}")
              }
          }
      }*/

    var userMessageSocket: Socket? = null


    var roomId: String = ""
    var from: String = ""
    var iAm: String = ""

    fun sendMessageTo(
        content: String, callback: ReceiveSendMessageCallback
    ) {
        val data = JSONObject().put(
            "message",
            content
        )/*.put("from", *//*this.*//*iAm).put("room_id", roomId)*/

        userMessageSocket?.emit("SEND_MESSAGE", data, object : Ack {
            override fun call(vararg args: Any?) {
                Timber.tag("MessageSocketClass").d("callSEND_MESSAGE: ")
                callback.responseMessage2(content)
            }
        })

    }

    private fun sendMessageTo(callback: ReceiveSendMessageCallback) {
        userMessageSocket?.on("ALL_CHAT") { args ->
            try {
                val receiveMessage = gson.fromJson(args[0].toString(), Doc::class.java)
                Timber.tag("MessageSocketClass").d(
                    "RECIEVED_MESSAGE1212: ${args[0].toString()} message12312312321 ${receiveMessage.message}"
                )
                callback.onGetReceiveMessage(receiveMessage)
            } catch (e: java.lang.Exception) {
                Timber.tag("MessageSocketClass").d("RECIEVED_MESSAGE: ${args[0]}")
            }
        }

    }


    /* fun sendMessageTo(
         imgUrl: String, content: String, callback: ReceiveSendMessageCallback
     ) {

         val data =
             JSONObject().put("content", content).put("media", imgUrl).put("from", *//*from*//*iAm)
                .put("room_id", roomId)

        userMessageSocket?.emit("SEND_MESSAGE", data, object : Ack {
            override fun call(vararg args: Any?) {
                Timber.tag("MessageSocketClass").d("callSEND_MESSAGE: ")
                callback.responseMessage2(content, imgUrl)
            }
        })

    }*/

//    interface GetAllChatCallBack {
//        fun getAllChatsModel(getAllChatsData: GetAllChatsData)
//        fun responseCHATALL(getAllChatsData: GetAllChatsData)
//    }

//    fun getAllChatsPost(roomId: String = "63e37feb1c70acaa38abc392", callback: GetAllChatCallBack) {
//        val data = JSONObject().put("user_id", roomId)
//        getAllChats(callback)
//        getAllChats2(callback)
//        userMessageSocket?.emit("ALL_CHATS", data, object : Ack {
//            override fun call(vararg args: Any?) {
//                Timber.tag("TAG").d("call: ")
//            }
//        })
//
//        Timber.tag("TAG").d("getAllChatsPost: ")
//
//    }

    fun initiateChat(orderId: String) {
        val arr = listOf(orderId)
//        val data = JSONObject().put("orderId", "$arr")
        val data = JSONObject().put("orderId", orderId)
        Timber.tag("MessageSocketClass").d("initiateChat:$data ")
        userMessageSocket?.emit("INICIATE_CHAT", data, object : Ack {
            override fun call(vararg args: Any?) {
                Timber.tag("MessageSocketClass").d("initiatedChat: ")
            }
        })
    }

    interface GetAllMessageCallBack {
        fun onGetAllMessage(getAllChatsData: GetAllMessageData)
        fun responseMessage()
    }

    interface ReceiveSendMessageCallback {
        fun onGetReceiveMessage(getAllChatsData: Doc)
        fun responseMessage2(str: String)
//        fun responseMessage2(str: String, strImg: String)
    }

    fun getAllMessage(
        limit: Int, page: Int, callback: GetAllMessageCallBack
    ) {
        val data = JSONObject().put("limit", 1000).put("page", 1)

        userMessageSocket?.emit("ALL_CHAT", data, object : Ack {
            override fun call(vararg args: Any?) {
                Timber.tag("MessageSocketClass").d("Recieve12121212")
                callback.responseMessage()
            }
        })
        getAllMessagesListener(callback)

    }


    fun disconnect() {
        userMessageSocket?.disconnect()
        userMessageSocket?.on(Socket.EVENT_DISCONNECT) {
            Timber.tag("MessageSocketClass")
                .d("EVENT_DISCONNECT12: ${userMessageSocket?.connected()}")
        }
    }


    //Events to listen for
    //MEssage to Emit
    //Listener provide runtime data
    //always disconnect socket when changing fragment
    //always start socket when when id is available
    //always disconnect socket and null it if it is not connect for the first three time otherwise it will take memory and crash


}