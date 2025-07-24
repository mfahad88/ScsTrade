package com.example.scstrade.views.stockscreener.customscreener

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    private val selectedColumns: List<String>,
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
        fun bind(selectedColumns: List<String>) {
            toggle(binding.price, "Share Price", selectedColumns)
            toggle(binding.priceToEarning, "Price to Earnings (P/E)", selectedColumns)
            toggle(binding.expectedPriceToEarning, "Expected Price to Earnings", selectedColumns)
            toggle(binding.dividendYield, "Dividend Yield", selectedColumns)
            toggle(binding.priceToBookValue, "Price to Book Value (P/B)", selectedColumns)
            toggle(binding.returnOnAssets, "Return on Assets (ROA)", selectedColumns)
            toggle(binding.returnOnEquity, "Return on Equity (ROE)", selectedColumns)
            toggle(binding.ebitaMargin, "EBITDA Margin", selectedColumns)
            toggle(binding.enterpriseValueToEbitda, "EV to EBITDA", selectedColumns)
            toggle(binding.grossProfitMargin, "Gross Profit Margin", selectedColumns)
            toggle(binding.payoutRatio, "Payout Ratio", selectedColumns)
            toggle(binding.priceEarningGrowth, "Price Earning Growth", selectedColumns)
            toggle(binding.totalDebtToAssets, "Total Debt to Assets", selectedColumns)
            toggle(binding.totalDebtToEquity, "Total Debt to Equity", selectedColumns)
        }

        private fun toggle(view: View, key: String, selected: List<String>) {
            view.visibility = if (key in selected) View.VISIBLE else View.GONE
        }
    }

    class CustomScreenerViewHolder(val binding: ItemStockScreenerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: StockScreenerItem,
            stockItem: StockItem?,
            selectedColumns: List<String>,
            onItemClick: (StockScreenerItem) -> Unit
        ) {
            binding.apply {
                Utils.getCompanyLogo(binding.root.context, binding.imageViewLogo, stockItem)

                symbol.text = item.symbol
                companyName.text = stockItem?.nM ?: "-"

                toggle(price, "Share Price", selectedColumns, formatDouble(item.price))
                toggle(priceToEarning, "Price to Earnings (P/E)", selectedColumns, formatDouble(item.priceToEarning))
                toggle(expectedPriceToEarning, "Expected Price to Earnings", selectedColumns, formatDouble(item.expectedPriceToEarning))
                toggle(dividendYield, "Dividend Yield", selectedColumns, formatDouble(item.dividendYield))
                toggle(priceToBookValue, "Price to Book Value (P/B)", selectedColumns, formatDouble(item.priceToBookValue))
                toggle(returnOnAssets, "Return on Assets (ROA)", selectedColumns, formatDouble(item.returnOnAssets))
                toggle(returnOnEquity, "Return on Equity (ROE)", selectedColumns, formatDouble(item.returnOnEquity))
                toggle(ebitaMargin, "EBITDA Margin", selectedColumns, formatDouble(item.eBITAMargin))
                toggle(enterpriseValueToEbitda, "EV to EBITDA", selectedColumns, formatDouble(item.enterpriseValueToEBITDA))
                toggle(grossProfitMargin, "Gross Profit Margin", selectedColumns, formatDouble(item.grossProfitMargin))
                toggle(payoutRatio, "Payout Ratio", selectedColumns, formatDouble(item.payoutRatio))
                toggle(priceEarningGrowth, "Price Earning Growth", selectedColumns, formatDouble(item.priceEarningGrowth))
                toggle(totalDebtToAssets, "Total Debt to Assets", selectedColumns, formatDouble(item.totalDebtToAssets))
                toggle(totalDebtToEquity, "Total Debt to Equity", selectedColumns, formatDouble(item.totalDebtToEquity))

                root.setOnClickListener { onItemClick(item) }
            }
        }

        private fun toggle(view: View, key: String, selected: List<String>, value: String) {
            if (key in selected) {
                view.visibility = View.VISIBLE
                if (view is TextView) view.text = value
            } else {
                view.visibility = View.GONE
            }
        }

        private fun formatDouble(value: Double?): String {
            return if (value == null || value == 0.0) {
                "N/A"
            } else {
                String.format("%,.2f", value)
            }
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
            holder.bind(selectedColumns)
            ScrollSyncHelper.register(holder.binding.horizontalScroll)
        } else if (holder is CustomScreenerViewHolder) {
            val item = itemList[position - 1] // account for header
            val stockItem = symbolMap[item.symbol.uppercase()]
            holder.bind(item, stockItem, selectedColumns, onItemClick)
            ScrollSyncHelper.register(holder.binding.horizontalScroll)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        ScrollSyncHelper.unregister(
            when (holder) {
                is HeaderViewHolder -> holder.binding.horizontalScroll
                is CustomScreenerViewHolder -> holder.binding.horizontalScroll
                else -> return
            }
        )
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
