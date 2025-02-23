package com.example.scstrade.views.allstock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemStocksBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.stock.StockItem
import java.util.Collections

class StockAdapter(private var list:List<StockItem>,var isMore:Boolean=false):RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    inner class StockViewHolder( val binding: ItemStocksBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(stockItem: StockItem) {

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


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {

       val binding=ItemStocksBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  StockViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(list[position])

        if(isMore){

            holder.apply {
                binding.layoutMore.visibility=View.VISIBLE
                binding.moreDetail.setOnClickListener {
                    if(binding.layoutDetails.visibility==View.GONE){
                        binding.layoutDetails.visibility=View.VISIBLE
                        binding.moreDetail.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.drop_down_icon,0)
                    }else{
                        binding.layoutDetails.visibility=View.GONE
                        binding.moreDetail.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.drop_up_icon,0)
                    }
                }
            }
        }
    }

    public fun addItems(list:List<StockItem>){
//        val diffCallback=WatchListDetailDiffCallback(this.list,list)
//        val diffResult=DiffUtil.calculateDiff(diffCallback)
        this.list=list
        notifyDataSetChanged()
//        diffResult.dispatchUpdatesTo(this)
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