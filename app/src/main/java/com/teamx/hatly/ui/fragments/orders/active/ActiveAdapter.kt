package com.teamx.hatly.ui.fragments.orders.active

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.teamx.hatly.databinding.ItemActiveListBinding


class ActiveAdapter(
    val arrayList: ArrayList<com.teamx.hatly.data.dataclasses.getActiveorder.Doc>) : RecyclerView.Adapter<ActiveAdapter.TopProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemTopProductBinding = ItemActiveListBinding.inflate(inflater, parent, false)
        return TopProductViewHolder(itemTopProductBinding)

    }


    override fun onBindViewHolder(holder: TopProductViewHolder, position: Int) {

        val list: com.teamx.hatly.data.dataclasses.getActiveorder.Doc = arrayList[position]

//        holder.binding.textView13.text = list.pickup.toString()
//        holder.binding.address.text = list.dropOff.toString()


        try {


            holder.binding.textView23.text = list.orders.products[0].productName

            Picasso.get().load(list.orders.products[0].image).into(holder.binding.imgIcon)


            val inputString = list.pickup.formattedAddress

            val streetAddressRegex = Regex("Street Address: (.*?),")
            val localityRegex = Regex("Locality: (.*?),")

            val streetAddressMatch = streetAddressRegex.find(inputString)
            val localityMatch = localityRegex.find(inputString)

            if (streetAddressMatch != null && localityMatch != null) {
                val streetAddress = streetAddressMatch.groupValues[1]
                val locality = localityMatch.groupValues[1]

                // Combine street address and locality into a single string
                val combinedAddress = "$streetAddress, $locality"
                holder.binding.textView24.text = combinedAddress

            } else {
                println("Street address and/or locality not found in the input string.")
            }



            holder.binding.textView27.text = list.orders.products[0].productName
            holder.binding.textView28.text = list.orders.products[0].prize.toString()+" AED"
            holder.binding.textView31.text = list.orders.total.toString()
            holder.binding.textView32.text = list.customer.name
            holder.binding.textView33.text = list.dropOff.address
            holder.binding.textView35.text = list.orders.specialNote
            holder.binding.ratingBar.rating = list.averageRating.toFloat()
        }
        catch (e:Exception){

        }

//        holder.binding.price.text = list.dropOff.toString()+"AED"


        holder.itemView.setOnClickListener {}


    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class TopProductViewHolder(itemTopProductBinding: ItemActiveListBinding) :
        RecyclerView.ViewHolder(itemTopProductBinding.root) {
        val binding = itemTopProductBinding

    }
}