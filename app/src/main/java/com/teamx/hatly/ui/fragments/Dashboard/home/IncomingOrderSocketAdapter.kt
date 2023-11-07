package com.teamx.hatly.ui.fragments.Dashboard.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.hatly.databinding.ItemIncomingListBinding
import com.teamx.hatly.ui.fragments.chat.socket.model.incomingOrderSocketData.IncomingOrderSocketData


class IncomingOrderSocketAdapter(
    val arrayList: ArrayList<IncomingOrderSocketData>,
    private val onAcceptRejectSocket: onAcceptRejectSocket
) : RecyclerView.Adapter<IncomingOrderSocketAdapter.TopProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemTopProductBinding = ItemIncomingListBinding.inflate(inflater, parent, false)
        return TopProductViewHolder(itemTopProductBinding)

    }


    override fun onBindViewHolder(holder: TopProductViewHolder, position: Int) {

        val list: IncomingOrderSocketData = arrayList[position]

        try {


            holder.binding.pickup.address.text = list.dropOff.address

            val inputString = list.shop.address.streetAddress

            val streetAddressRegex = Regex("Street Address: (.*?),")
            val localityRegex = Regex("Locality: (.*?),")

            val streetAddressMatch = streetAddressRegex.find(inputString)
            val localityMatch = localityRegex.find(inputString)

            if (streetAddressMatch != null && localityMatch != null) {
                val streetAddress = streetAddressMatch.groupValues[1]
                val locality = localityMatch.groupValues[1]

                // Combine street address and locality into a single string
                val combinedAddress = "$streetAddress, $locality"
                holder.binding.pickup.textView13.text = combinedAddress

            } else {
                println("Street address and/or locality not found in the input string.")
            }



            holder.binding.pickup.price.text = list.products[0].prize.toString()
        }
        catch (e:Exception){

        }

        holder.binding.buttons.btnAccept.setOnClickListener {
            onAcceptRejectSocket.onAcceptSokcetClick(position)
        }
        holder.binding.buttons.btnReject.setOnClickListener {
            onAcceptRejectSocket.onRejectSocketClick(position)
        }

    }

    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount1212: ${arrayList.size}")
        return arrayList.size
    }

    class TopProductViewHolder(itemTopProductBinding: ItemIncomingListBinding) :
        RecyclerView.ViewHolder(itemTopProductBinding.root) {
        val binding = itemTopProductBinding

    }
}

interface onAcceptRejectSocket{
    fun onAcceptSokcetClick(position: Int)
    fun onRejectSocketClick(position: Int)

}