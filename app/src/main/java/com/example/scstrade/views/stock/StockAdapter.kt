package com.example.scstrade.views.stock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scstrade.databinding.ItemStocksBinding
import com.example.scstrade.model.response.stock.StockItem

class StockAdapter(private var list:List<StockItem>):RecyclerView.Adapter<StockAdapter.StockViewHolder>() {
    inner class StockViewHolder(private val binding: ItemStocksBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(stockItem: StockItem) {
//            Glide.with(binding.root.context).load(stockItem.companyLogo).into(binding.imageView6)
            if(stockItem.iN.lowercase().contains("kmi")){
                binding.shariah.visibility= View.VISIBLE
            }else{
                binding.shariah.visibility= View.GONE
            }
            binding.symbol.text = stockItem.sYM
            binding.companyName.text = stockItem.nM
            binding.volume.text = "Vol: ${stockItem.v}"
            binding.bidVol.text = "Bid Vol: ${stockItem.bV}"
            binding.bid.text = "Bid: ${stockItem.bP}"
            binding.askVol.text = "Ask Vol: ${stockItem.aV}"
            binding.ask.text = "Ask: ${stockItem.aP}"
            binding.valueTrade.text = stockItem.cL.toString()
            binding.netChange.text = "${stockItem.cH} (${String.format("%.2f",stockItem.cHP)}%)"
            binding.high.text = stockItem.hP.toString()
            binding.low.text = stockItem.lP.toString()
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
    }

    public fun addItem(list:List<StockItem>){
        this.list=list
        notifyDataSetChanged()
    }
}