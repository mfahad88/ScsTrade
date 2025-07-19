package com.example.scstrade.views.portfolio.adapter
import androidx.compose.ui.res.dimensionResource

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scstrade.databinding.ItemMyPortfolioHoldingBinding
import com.example.scstrade.databinding.ItemShareInHandBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.portfolio.FifoPortfolio
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.viewmodels.SharedViewModel

class ShareInHandAdapter(/*private var itemList:List<FifoPortfolio>,private var list:List<StockItem>*/
                         private val sharedViewModel: SharedViewModel,
                         private val onItemClick: (FifoPortfolio) -> Unit
) : ListAdapter<FifoPortfolio,ShareInHandAdapter.ShareInHandViewHolder>(ShareInHandDiffCallback()) {

    class ShareInHandDiffCallback:DiffUtil.ItemCallback<FifoPortfolio>(){
        override fun areItemsTheSame(oldItem: FifoPortfolio, newItem: FifoPortfolio): Boolean {
            return oldItem.portfolioMainID.equals(newItem.portfolioMainID)
        }

        override fun areContentsTheSame(oldItem: FifoPortfolio, newItem: FifoPortfolio): Boolean {
           return oldItem==newItem
        }

    }
    class ShareInHandViewHolder(private val binding: ItemMyPortfolioHoldingBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item:FifoPortfolio,stockItem: StockItem) {


            val marketValue =item.quantity.toDouble().times(stockItem.cL)
            val totalCost   =item.quantity.toDouble().times(item.price.toDouble())
            val dayPL   = item.quantity.toDouble().times(stockItem.cH)
            val dayPLPercent = stockItem.cHP
            val totalPL = item.quantity.toDouble().times(stockItem.cL).minus(totalCost)
            val totPLPercent = totalPL.div(totalCost).times(100)


            Utils.getCompanyLogo(binding.root.context,binding.imgLogo,stockItem)
            binding.tvSymbol.text = item.symbol
            binding.tvPrice.text = Utils.roundTwoDecimal(stockItem.cL)
            binding.tvShares.text = item.quantity
            binding.tvAvgBuy.text = item.price
            binding.tvTotalCost.text = Utils.roundTwoDecimal(totalCost)
            binding.tvMarketValue.text = Utils.roundTwoDecimal(marketValue)
            binding.tvDayPL.text = Utils.roundTwoDecimal(dayPL)
            binding.tvDayPLPercent.text = "${Utils.roundTwoDecimal(dayPLPercent)}%"
            binding.tvTotalPL.text = Utils.roundTwoDecimal(totalPL)
            binding.tvTotalPLPercent.text = "${Utils.roundTwoDecimal(totPLPercent)}%"
            binding.tvPriceChange.text = "${stockItem.cH}"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareInHandViewHolder {
        val binding = ItemMyPortfolioHoldingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShareInHandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShareInHandViewHolder, position: Int) {

        val list=sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(getItem(position).symbol,true) }?.first()

        if (list != null) {
            holder.bind(getItem(position),list)
            holder.itemView.setOnClickListener {
                onItemClick(getItem(position))
            }
        }
    }




}
