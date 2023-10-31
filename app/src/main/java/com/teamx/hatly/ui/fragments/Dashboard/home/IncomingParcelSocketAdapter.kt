package com.teamx.hatly.ui.fragments.Dashboard.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.hatly.databinding.ItemIncomingListBinding
import com.teamx.hatly.ui.fragments.chat.socket.model.incomingParcelSoocketData.IncomingParcelSocketData


class IncomingParcelSocketAdapter(val arrayList: ArrayList<IncomingParcelSocketData>) : RecyclerView.Adapter<IncomingParcelSocketAdapter.TopProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemTopProductBinding = ItemIncomingListBinding.inflate(inflater, parent, false)
        return TopProductViewHolder(itemTopProductBinding)

    }


    override fun onBindViewHolder(holder: TopProductViewHolder, position: Int) {

        val list: IncomingParcelSocketData = arrayList[position]

        try {


            holder.binding.pickup.address.text = list.dropOff.address

            val inputString = list.pickup.address

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



            holder.binding.pickup.price.text = list.parcel.fare.toString()
        }
        catch (e:Exception){

        }

 /*       holder.binding.buttons.btnAccept.setOnClickListener {
            onAccepetReject.onAcceptClick(position)
        }
        holder.binding.buttons.btnReject.setOnClickListener {
            onAccepetReject.onRejectClick(position)
        }*/

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

/*
interface onAcceptReject{
    fun onAcceptClick(position: Int)
    fun onRejectClick(position: Int)

}*/
