package com.teamx.hatly.ui.fragments.orders.Incoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.hatly.data.dataclasses.getOrderStatus.Doc
import com.teamx.hatly.databinding.ItemIncomingListBinding


class IncomingAdapter(
    val arrayList: ArrayList<Doc>,
    private val onAccepetReject: onAcceptReject
) : RecyclerView.Adapter<IncomingAdapter.TopProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemTopProductBinding = ItemIncomingListBinding.inflate(inflater, parent, false)
        return TopProductViewHolder(itemTopProductBinding)

    }


    override fun onBindViewHolder(holder: TopProductViewHolder, position: Int) {

        val list: Doc = arrayList[position]

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



            holder.binding.pickup.price.text = list.parcel.fare.toString() + "AED"
        }
        catch (e:Exception){

        }

        holder.binding.buttons.btnAccept.setOnClickListener {
            onAccepetReject.onAcceptClick(position)
        }
        holder.binding.buttons.btnReject.setOnClickListener {
            onAccepetReject.onRejectClick(position)
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class TopProductViewHolder(itemTopProductBinding: ItemIncomingListBinding) :
        RecyclerView.ViewHolder(itemTopProductBinding.root) {
        val binding = itemTopProductBinding

    }
}

interface onAcceptReject{
    fun onAcceptClick(position: Int)
    fun onRejectClick(position: Int)

}