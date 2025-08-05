package com.example.scstrade.views.globalmarket

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemGlobalMarketBinding
import com.example.scstrade.model.response.globalMarket.GlobalMarketItem
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

class GlobalStockAdapter(
    private val onItemClick: (GlobalMarketItem) -> Unit
) : ListAdapter<GlobalMarketItem, GlobalStockAdapter.GlobalStockViewHolder>(StockDiffCallback()) {

    private val previousPrices = mutableMapOf<String, Double>()

    class GlobalStockViewHolder(
        private val binding: ItemGlobalMarketBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: GlobalMarketItem,
            previousPrice: Double?,
            onItemClick: (GlobalMarketItem) -> Unit
        ) {
            binding.apply {
                symbol.text = item.worldMarketName

                // 🔀 Apply conditional formatting
                valueTrade.text = formatValue(item.worldMarketPrice, item.worldMarketType)

                netChange.text = "${if (item.worldMarketChange < 0.0) "" else "+"}${formatValue(item.worldMarketChange, item.worldMarketType)} " +
                        "${if (item.worldMarketChangeP < 0.0) "" else "+"}${formatValue(item.worldMarketChangeP, item.worldMarketType)}%"

                if (item.worldMarketChange < 0.0) {
                    netChange.setTextColor(ContextCompat.getColor(root.context, R.color.md_theme_error))
                } else if (item.worldMarketChange > 0.0) {
                    netChange.setTextColor(ContextCompat.getColor(root.context, R.color.md_theme_primary))
                } else {
                    netChange.setTextColor(Color.parseColor("#1A73E8"))
                }

                high.text = "H: ${formatValue(item.worldMarketHigh, item.worldMarketType)}"
                low.text = "L: ${formatValue(item.worldMarketLow, item.worldMarketType)}"

                if (previousPrice == null) {
                    cardValueTrade.setCardBackgroundColor(Color.TRANSPARENT)
                    cardValueTrade.alpha = 1f
                } else {
                    val diff = BigDecimal(item.worldMarketPrice)
                        .setScale(6, RoundingMode.HALF_UP)
                        .toDouble()
                        .minus(previousPrice)

                    if (diff > 0) {
                        cardValueTrade.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.green_increse))
                    } else if (diff < 0) {
                        cardValueTrade.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.red_decrease))
                    } else {
                        cardValueTrade.setCardBackgroundColor(Color.TRANSPARENT)
                    }

                    cardValueTrade.postDelayed({
                        cardValueTrade.setCardBackgroundColor(Color.TRANSPARENT)
                    }, 3000)
                }

                root.setOnClickListener { onItemClick(item) }
            }
        }

        // 🔧 Conditional formatting function
        private fun formatValue(value: Double, marketType: String?): String {
            return if (marketType.equals("Currencies", ignoreCase = true)) {
                formatWithAllDecimals(value)
            } else {
                formatWithTwoDecimals(value)
            }
        }

        private fun formatWithAllDecimals(value: Double): String {
            val bd = BigDecimal.valueOf(value).stripTrailingZeros()
            val decimalPlaces = bd.scale().coerceAtLeast(0)
            val pattern = "#,##0" + if (decimalPlaces > 0) "." + "#".repeat(decimalPlaces) else ""
            return DecimalFormat(pattern).format(value)
        }

        private fun formatWithTwoDecimals(value: Double): String {
            val pattern = "#,##0.00"
            return DecimalFormat(pattern).format(value)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobalStockViewHolder {
        val binding = ItemGlobalMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GlobalStockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GlobalStockViewHolder, position: Int) {
        val current = getItem(position)
        val previousPrice = previousPrices[current.worldMarketName]
        holder.bind(current, previousPrice, onItemClick)
        previousPrices[current.worldMarketName] = current.worldMarketPrice
    }

    private class StockDiffCallback : DiffUtil.ItemCallback<GlobalMarketItem>() {
        override fun areItemsTheSame(oldItem: GlobalMarketItem, newItem: GlobalMarketItem): Boolean {
            return oldItem.worldMarketName == newItem.worldMarketName
        }

        override fun areContentsTheSame(oldItem: GlobalMarketItem, newItem: GlobalMarketItem): Boolean {
            return oldItem == newItem
        }
    }
}
