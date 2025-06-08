package com.example.scstrade.views.allstock

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemStocksBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.views.snapshot.SnapshotActivity
import java.util.Collections

class StockAdapter(/*private var list:MutableList<StockItem>,*/var isMore:Boolean=false):ListAdapter<StockItem,StockAdapter.StockViewHolder>(StockDiffCallback()) {
    var previousStockItem:StockItem?=null
    inner class StockViewHolder( val binding: ItemStocksBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(stockItem: StockItem) {
            binding.root.setOnClickListener {
                val intent= Intent(binding.root.context,SnapshotActivity::class.java)
                intent.putExtra(AppConstants.SYMBOL,stockItem.sYM)
                binding.root.context.startActivity(intent)
            }
            Glide.with(binding.root.context).load(stockItem.companyLogo).circleCrop().into(binding.imageView6)
            if(stockItem.iN.lowercase().contains("kmi")){
                binding.shariah.visibility= View.VISIBLE
                binding.separator.visibility = View.VISIBLE
            }else{
                binding.shariah.visibility= View.GONE
                binding.separator.visibility = View.GONE
            }
            binding.symbol.text = stockItem.sYM
            binding.companyName.text = stockItem.nM
            binding.volume.text = "Vol: ${Utils.convertToMillions(stockItem.v.toDouble())}"


            binding.bidVol.text = "Bid Vol: ${Utils.convertToMillions(stockItem.bV.toDouble())}"
            binding.bid.text = "Bid: ${stockItem.bP}"
            binding.askVol.text = "Ask Vol: ${Utils.convertToMillions(stockItem.aV.toDouble())}"
            binding.ask.text = "Ask: ${stockItem.aP}"
            if(previousStockItem?.cL?.compareTo(stockItem?.cL?:0.0)!=0){
                Utils.animatedValueChange(previousStockItem?.cL?:0.0,stockItem.cL, onUpdate = {
                    binding.valueTrade.text = String.format("%.2f",stockItem.cL)
                })
            }
            binding.netChange.text = "${if(stockItem.cH<0.0) "" else "+"}${Utils.formatDouble(stockItem.cH)} (${if(stockItem.cH<0.0) "" else "+"} ${Utils.formatDouble(stockItem.cHP)}%)"
            if(stockItem.cH<0.0){
                binding.netChange.setTextColor(ContextCompat.getColor(binding.root.context,R.color.md_theme_error))
            }else{
                binding.netChange.setTextColor(ContextCompat.getColor(binding.root.context,R.color.md_theme_primary))
            }

            binding.high.text = "H: ${Utils.formatDouble(stockItem.hP)}"
            binding.low.text = "L: ${Utils.formatDouble(stockItem.lP)}"
            binding.high52.text = stockItem.high52
            binding.low52.text = stockItem.low52
            previousStockItem=stockItem

        }


    }

    private class StockDiffCallback :  DiffUtil.ItemCallback<StockItem>(){
        override fun areItemsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem.sYM.equals(newItem.sYM)
        }

        override fun areContentsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem==newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {

        val binding=ItemStocksBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(getItem(position))



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



}