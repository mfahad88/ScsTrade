package com.example.scstrade.views.watchlist

import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.scstrade.databinding.ItemWatchlistDetailBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.stock.StockItem
import java.util.Collections

class WatchListDetailAdapter(var list:List<StockItem>, val onItemClick: (String,StockItem) -> Unit) : RecyclerView.Adapter<WatchListDetailAdapter.WatchListDetailViewHolder>() {

    class WatchListDetailViewHolder(private val binding: ItemWatchlistDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(stockItem: StockItem, onItemClick: (String,StockItem) -> Unit) {

//            Glide.with(binding.root.context).load(stockItem.companyLogo).into(binding.imageView6)
            if(stockItem.iN.lowercase().contains("kmi")){
                binding.shariah.visibility= View.VISIBLE
            }else{
                binding.shariah.visibility= View.GONE
            }
            binding.symbol.text = stockItem.sYM
            binding.companyName.text = stockItem.nM
            binding.volume.text = "Vol: ${Utils.convertToMillions(stockItem.v.toDouble())}"
            binding.bidVol.text = "Bid Vol: ${Utils.convertToMillions(stockItem.bV.toDouble())}"
            binding.bid.text = "Bid: ${stockItem.bP}"
            binding.askVol.text = "Ask Vol: ${Utils.convertToMillions(stockItem.aV.toDouble())}"
            binding.ask.text = "Ask: ${stockItem.aP}"
            binding.valueTrade.text = String.format("%.2f",stockItem.cL)
            binding.netChange.text = "${stockItem.cH} (${String.format("%.2f",stockItem.cHP)}%)"
            binding.high.text = "H: ${stockItem.hP.toString()}"
            binding.low.text = "L: ${stockItem.lP.toString()}"
            binding.high52.text = stockItem.high52
            binding.low52.text = stockItem.low52

            binding.imageViewThree.setOnClickListener {
                Utils.showPopup(binding.root.context,it,null, listOf("Delete Company")){
                    onItemClick(it,stockItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchListDetailViewHolder {
        val binding = ItemWatchlistDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchListDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchListDetailViewHolder, position: Int) {
        holder.bind(list[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    public fun addItems(list:List<StockItem>){

        this.list=list

        notifyDataSetChanged()
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        Collections.swap(list, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                swapItems(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // No swipe action needed
            }
        })
    }
}
