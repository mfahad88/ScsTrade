package com.example.scstrade.views.stockscreener.customscreener

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scstrade.databinding.ItemScreenerHeaderBinding
import com.example.scstrade.databinding.ItemStockScreenerBinding

import com.example.scstrade.databinding.ItemStockScreenerRowBinding
import com.example.scstrade.helper.ScrollSyncHelper
import com.example.scstrade.model.response.stockscreener.StockScreenerItem
import com.example.scstrade.viewmodels.SharedViewModel
import kotlin.reflect.full.memberProperties

class CustomScreenerAdapter(private val itemList: MutableList<StockScreenerItem>?,val sharedViewModel: SharedViewModel, private val onItemClick: (StockScreenerItem) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ROW = 1
    }
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ROW
    }
    class CustomScreenerViewHolder(val binding: ItemStockScreenerRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StockScreenerItem?,sharedViewModel: SharedViewModel, onItemClick: (StockScreenerItem) -> Unit) {
            val stockItem=sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(item?.symbol,true) }?.first()
            binding.apply {
                Glide.with(binding.root.context).load(stockItem?.companyLogo).circleCrop().into(binding.imageViewLogo)
                symbol.text = item?.symbol
                companyName.text = stockItem?.nM
                dividendYield.text = item?.dividendYield.toString()
                ebitaMargin.text = item?.eBITAMargin.toString()
                enterpriseValueToEbitda.text = item?.enterpriseValueToEBITDA.toString()
                expectedPriceToEarning.text = item?.expectedPriceToEarning.toString()
                grossProfitMargin.text = item?.grossProfitMargin.toString()
                payoutRatio.text = item?.payoutRatio.toString()
                priceEarningGrowth.text = item?.priceEarningGrowth.toString()
                priceToBookValue.text = item?.priceToBookValue.toString()
                priceToEarning.text = item?.priceToEarning.toString()
                returnOnAssets.text = item?.returnOnAssets.toString()
                returnOnEquity.text = item?.returnOnEquity.toString()
                totalDebtToAssets.text = item?.totalDebtToAssets.toString()
                totalDebtToEquity.text = item?.totalDebtToEquity.toString()

            }

            binding.root.setOnClickListener {
                if (item != null) {
                    onItemClick(item)
                }
            }
        }
    }

    class HeaderViewHolder(val binding: ItemScreenerHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            // Static XML headers – no binding needed
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemScreenerHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            else -> {
                val binding = ItemStockScreenerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CustomScreenerViewHolder(binding)
            }
        }
        /*val binding = ItemStockScreenerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomScreenerViewHolder(binding)*/
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder.bind(itemList?.get(position),sharedViewModel, onItemClick)
        if (holder is CustomScreenerViewHolder && position > 0) {
            holder.bind(itemList?.get(position - 1),sharedViewModel, onItemClick) // Subtract 1 for header offset
            ScrollSyncHelper.register((holder as CustomScreenerViewHolder).binding.horizontalScroll)
        } else if (holder is HeaderViewHolder) {
            holder.bind()
            ScrollSyncHelper.register((holder as HeaderViewHolder).binding.horizontalScroll)
        }

    }

    override fun getItemCount(): Int {
        return itemList?.size?:0
    }

}
