package com.example.scstrade.views.stockscreener.customscreener

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemScreenerHeaderBinding
import com.example.scstrade.databinding.ItemStockScreenerRowBinding
import com.example.scstrade.helper.ScrollSyncHelper
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.response.stockscreener.StockScreenerItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.widgets.TextDrawable

class CustomScreenerAdapter(
    private val itemList: MutableList<StockScreenerItem>,
    private val symbolMap: Map<String, StockItem>,
    private val onItemClick: (StockScreenerItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ROW = 1
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return if (position == 0) -1L
        else itemList[position - 1].symbol.hashCode().toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ROW
    }

    class HeaderViewHolder(val binding: ItemScreenerHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            // Static header — no binding required
        }
    }

    class CustomScreenerViewHolder(val binding: ItemStockScreenerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: StockScreenerItem,
            stockItem: StockItem?,
            onItemClick: (StockScreenerItem) -> Unit
        ) {
            binding.apply {
               Utils.getCompanyLogo(binding.root.context,binding.imageViewLogo,stockItem)

                symbol.text = item.symbol
                companyName.text = stockItem?.nM ?: "-"
                price.text = formatDouble(item.price)
                dividendYield.text = formatDouble(item.dividendYield)
                ebitaMargin.text = formatDouble(item.eBITAMargin)
                enterpriseValueToEbitda.text = formatDouble(item.enterpriseValueToEBITDA)
                expectedPriceToEarning.text = formatDouble(item.expectedPriceToEarning)
                grossProfitMargin.text = formatDouble(item.grossProfitMargin)
                payoutRatio.text = formatDouble(item.payoutRatio)
                priceEarningGrowth.text = formatDouble(item.priceEarningGrowth)
                priceToBookValue.text = formatDouble(item.priceToBookValue)
                priceToEarning.text = formatDouble(item.priceToEarning)
                returnOnAssets.text = formatDouble(item.returnOnAssets)
                returnOnEquity.text = formatDouble(item.returnOnEquity)
                totalDebtToAssets.text = formatDouble(item.totalDebtToAssets)
                totalDebtToEquity.text = formatDouble(item.totalDebtToEquity)

                root.setOnClickListener { onItemClick(item) }
            }
        }

        private fun formatDouble(value: Double?): String {
            return value?.let { String.format("%.2f", it) } ?: "-"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val binding = ItemScreenerHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HeaderViewHolder(binding)
        } else {
            val binding = ItemStockScreenerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CustomScreenerViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind()
            ScrollSyncHelper.register(holder.binding.horizontalScroll)
        } else if (holder is CustomScreenerViewHolder) {
            val item = itemList[position - 1] // account for header
            val stockItem = symbolMap[item.symbol.uppercase()]
            holder.bind(item, stockItem, onItemClick)
            ScrollSyncHelper.register(holder.binding.horizontalScroll)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is HeaderViewHolder) {
            ScrollSyncHelper.unregister(holder.binding.horizontalScroll)
        } else if (holder is CustomScreenerViewHolder) {
            ScrollSyncHelper.unregister(holder.binding.horizontalScroll)
        }
    }

    override fun getItemCount(): Int = itemList.size + 1

    fun updateList(newList: List<StockScreenerItem>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = itemList.size
            override fun getNewListSize() = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return itemList[oldItemPosition].symbol == newList[newItemPosition].symbol
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return itemList[oldItemPosition] == newList[newItemPosition]
            }
        })

        itemList.clear()
        itemList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}
