package com.teamx.hatly.ui.fragments.chat.socket

import com.google.gson.Gson
import com.teamx.hatly.constants.AppConstants.ApiConfiguration.Companion.BASE_URL_CHAT
import com.teamx.hatly.data.models.socket_models.ExceptionData
import com.teamx.hatly.ui.fragments.chat.socket.model.incomingOrderSocketData.IncomingOrderSocketData
import com.teamx.hatly.ui.fragments.chat.socket.model.incomingParcelSoocketData.IncomingParcelSocketData
import com.teamx.hatly.ui.fragments.orders.Incoming.IncomingAdapter
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import timber.log.Timber

object RiderSocketClass {

    var riderSocket: Socket? = null

    fun connectRider(
        token: String,
        lat: String,
        lng: String,
        incomingOrderCallBack: IncomingOrderCallBack
    ) {

        val options = IO.Options()
        val headers: MutableMap<String, List<String>> = HashMap()


        headers["Authorization"] =
            listOf("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWNhdGlvbiI6eyJpdiI6IjZiNjQ3NTMzNjkzODM3NjM2ODMyNmIzOTM1MzczODY0IiwiZW5jcnlwdGVkRGF0YSI6IjM4OTFhZWVmYjBlZDgwZmU2ZDY3OWEwYWQzY2IzNGQyZWM3MDA4MDFjZWNiZDY0NDk4ZWZlOWEwZjMxMDNkMjEifSwidW5pcXVlSWQiOiI5YmJmMDY2MGRhNGQzYWJjOTYyNGI1ODkxYjU2NDciLCJpYXQiOjE2OTc1NTQ1MDIsImV4cCI6MTAzMzc1NTQ1MDJ9.wVcvv6arA3aHPWgXg-ruB2ZlbnxIhw8bDgGLwH2myyg")


        options.extraHeaders = headers


        val u: Boolean? = riderSocket?.connected()

        if (u == null) {
            riderSocket = IO.socket("$BASE_URL_CHAT/rider", options)
        } else if (!u) {
            riderSocket = IO.socket("$BASE_URL_CHAT/rider", options)


        }

        riderSocket?.connect()
        Timber.tag("MessageSocketClass").d("EVENT_CONNECT: ${riderSocket?.connected()}")

        riderSocket?.on(Socket.EVENT_CONNECT) {
            onListenerEverything()
            goOnline(lat, lng)


            riderSocket?.on("INCOMING_ORDERS") { args ->
                Timber.tag("MessageSocketClass").d("INCOMING_ORDERS: }${args.get(0)}")

                try {
                    Timber.tag("ChatSocketClass").d("Get_ALL_CHATS: ${args.get(0)}")
                    val exception =
                        gson.fromJson(args[0].toString(), IncomingOrderSocketData::class.java)
                    incomingOrderCallBack.onIncomingOrderRecieve(exception)
                } catch (e: java.lang.Exception) {
                    Timber.tag("ChatSocketClass").d("ExceptionData: ${args[0]}")
                }
            }

            riderSocket?.on("INCOMING_PARCEL") { args ->
                Timber.tag("MessageSocketClass").d("INCOMING_ORDERS: }${args.get(0)}")

                try {
                    Timber.tag("ChatSocketClass").d("Get_ALL_CHATS: ${args.get(0)}")
                    val exception1 =
                        gson.fromJson(args[0].toString(), IncomingParcelSocketData::class.java)
                    incomingOrderCallBack.onIncomingParcelRecieve(exception1)
                } catch (e: java.lang.Exception) {
                    Timber.tag("ChatSocketClass").d("ExceptionData: ${args[0]}")
                }
            }


            Timber.tag("MessageSocketClass").d("EVENT_CONNECT12: ${riderSocket?.connected()}")


        }
        riderSocket?.on(Socket.EVENT_CONNECT_ERROR) {
            Timber.tag("MessageSocketClass")
                .d("EVENT_CONNECT_ERROR22:${riderSocket?.connected()} ${it[0]}")

        }

    }


    val gson = Gson()
    lateinit var activeOrderArrayList: ArrayList<IncomingOrderSocketData>
    lateinit var incomingOrderAdapter: IncomingAdapter


    private fun onListenerEverything() {
        riderSocket?.on("exception") { args ->

            try {
                val exception = gson.fromJson(args[0].toString(), ExceptionData::class.java)
                Timber.tag("MessageSocketClass").d("ExceptionData: ${exception.message}")
            } catch (e: java.lang.Exception) {
                Timber.tag("MessageSocketClass").d("ExceptionData: ${args[0]}")

            }

        }
        riderSocket?.on("ONLINE") { args ->

            Timber.tag("MessageSocketClass").d("ONLINE: }${args.get(0)}")

//            sendMessageTo("${args.get(0)}")

        }
        riderSocket?.on("UPDATED_LOCATION") { args ->
            Timber.tag("MessageSocketClass").d("UPDATED_LOCATION: }${args.get(0)}")
        }
        /*     riderSocket?.on("INCOMING_ORDERS") { args ->
                 val INCOMINGSOCKETORDER = gson.fromJson(args[0].toString(), IncomingOrderSocketData::class.java)
                 activeOrderArrayList.add(INCOMINGSOCKETORDER)
                 incomingOrderAdapter.notifyDataSetChanged()
                 Timber.tag("MessageSocketClass").d("INCOMING_ORDERS: }${args.get(0)}")

             }*/
        riderSocket?.on("INCOMING_PARCEL") { args ->
            Timber.tag("MessageSocketClass").d("INCOMING_PARCEL: }${args.get(0)}")

        }
        riderSocket?.on("OFFLINE") { args ->
            Timber.tag("MessageSocketClass").d("OFFLINE: }${args.get(0)}")

        }

    }


    fun goOnline(let: String, lng: String) {
        val data = JSONObject().put("lat", let).put("lng", lng)
        Timber.tag("MessageSocketClass").d("GO_ONLINE:$data ")
        riderSocket?.emit("GO_ONLINE", data, object : Ack {
            override fun call(vararg args: Any?) {
                Timber.tag("MessageSocketClass").d("initiatedChat: ")
            }
        })
    }


    fun disconnect() {
        riderSocket?.disconnect()
        riderSocket?.on(Socket.EVENT_DISCONNECT) {
            Timber.tag("MessageSocketClass")
                .d("EVENT_DISCONNECT: ${riderSocket?.connected()}")
        }
    }


    //Events to listen for
    //MEssage to Emit
    //Listener provide runtime data
    //always disconnect socket when changing fragment
    //always start socket when when id is available
    //always disconnect socket and null it if it is not connect for the first three time otherwise it will take memory and crash


}

interface IncomingOrderCallBack {
    fun onIncomingOrderRecieve(incomingOrderSocketData: IncomingOrderSocketData)
    fun onIncomingParcelRecieve(incomingParcelSocketData: IncomingParcelSocketData)
}