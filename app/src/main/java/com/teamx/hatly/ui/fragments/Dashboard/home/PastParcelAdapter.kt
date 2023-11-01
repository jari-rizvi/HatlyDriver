package com.teamx.hatly.ui.fragments.Dashboard.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.hatly.data.dataclasses.getorders.PastDispatche
import com.teamx.hatly.databinding.ItemPastOrderBinding


class PastParcelAdapter(val arrayList: ArrayList<PastDispatche>) : RecyclerView.Adapter<PastParcelAdapter.TopProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemTopProductBinding = ItemPastOrderBinding.inflate(inflater, parent, false)
        return TopProductViewHolder(itemTopProductBinding)

    }


    override fun onBindViewHolder(holder: TopProductViewHolder, position: Int) {

        val list: PastDispatche = arrayList[position]

//        holder.binding.textView13.text = list.pickup.toString()
//        holder.binding.address.text = list.dropOff.toString()


        try {


            holder.binding.textView13.text = list.parcel.receiverLocation.location.address

            val inputString = list.parcel.receiverLocation.location.address

            val streetAddressRegex = Regex("Street Address: (.*?),")
            val localityRegex = Regex("Locality: (.*?),")

            val streetAddressMatch = streetAddressRegex.find(inputString)
            val localityMatch = localityRegex.find(inputString)

            if (streetAddressMatch != null && localityMatch != null) {
                val streetAddress = streetAddressMatch.groupValues[1]
                val locality = localityMatch.groupValues[1]

                // Combine street address and locality into a single string
                val combinedAddress = "$streetAddress, $locality"
                holder.binding.textView13.text = combinedAddress

            } else {
                println("Street address and/or locality not found in the input string.")
            }



            holder.binding.price.text = list.parcel.fare.toString() + "AED"
        }
        catch (e:Exception){

        }

//        holder.binding.price.text = list.dropOff.toString()+"AED"


        holder.itemView.setOnClickListener {}


    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class TopProductViewHolder(itemTopProductBinding: ItemPastOrderBinding) :
        RecyclerView.ViewHolder(itemTopProductBinding.root) {
        val binding = itemTopProductBinding

    }
}