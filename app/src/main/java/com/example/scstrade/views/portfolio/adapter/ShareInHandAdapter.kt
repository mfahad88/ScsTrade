package com.example.scstrade.views.portfolio.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemShareInHandBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.data.ShareInHand

class ShareInHandAdapter(val itemList: List<ShareInHand>, private val onItemClick: (ShareInHand) -> Unit,private val onItemClickSnapshot: (ShareInHand) -> Unit) : RecyclerView.Adapter<ShareInHandAdapter.ShareInHandViewHolder>() {

    class ShareInHandViewHolder(private val binding: ItemShareInHandBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item:ShareInHand, onItemClick: (ShareInHand) -> Unit,onItemClickSnapshot: (ShareInHand) -> Unit) {
            Log.e("Item--->",item.toString())
            binding.symbol.text = item.symbol
            binding.totalCostValue.text = item.totalCost
            binding.avgBuyValue.text = item.avgBuy
            binding.marketValue.text = item.marketValue
            binding.shareValue.text = item.share
            binding.daysPL.text =item.dayPL
            binding.daysPercentPL.text = "(${item.percentdayPL}%)"
            binding.totalPL.text = item.totalPL
            binding.totalPercentPL.text = "(${item.percenttotalPL}%)"
            binding.btnSell.setOnClickListener {
                onItemClick(item)
            }
            binding.threeDots.setOnClickListener {
                Utils.showPopup(binding.root.context,binding.threeDots,null, listOf("Edit Name")){

                }
            }
            binding.snapshotLogo.setOnClickListener {
                onItemClickSnapshot(item)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareInHandViewHolder {
        val binding = ItemShareInHandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShareInHandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShareInHandViewHolder, position: Int) {


        holder.bind(itemList[position], onItemClick,onItemClickSnapshot)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


}
