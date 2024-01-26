package com.teamx.hatlyDriver.ui.fragments.wallet.withdraw

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.teamx.hatlyDriver.data.dataclasses.withdrawalHistory.Doc
import com.teamx.hatlyDriver.databinding.ItemTransactionBinding
import com.teamx.hatlyDriver.utils.TimeFormatter


class WithdrawalAdapter(
    val arrayList: ArrayList<Doc>,
    private val onWithDrawalListener: OnWithDrawalListener
) : RecyclerView.Adapter<WithdrawalAdapter.TopProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemTransactionBinding = ItemTransactionBinding.inflate(inflater, parent, false)
        return TopProductViewHolder(itemTransactionBinding)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TopProductViewHolder, position: Int) {

        val list: Doc = arrayList[position]

        try {

            holder.binding.tvNoti.text = list.status
            holder.binding.amount.text = list.amount.toString()+" AED"

            holder.binding.time.text=  TimeFormatter.formatTimeDifference(list.createdAt.toString())

        }
        catch (e:Exception){

        }

        holder.itemView.setOnClickListener {
            onWithDrawalListener.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class TopProductViewHolder(itemTransactionBinding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(itemTransactionBinding.root) {
        val binding = itemTransactionBinding

    }
}
interface OnWithDrawalListener {
    fun onItemClick(position : Int)

}
