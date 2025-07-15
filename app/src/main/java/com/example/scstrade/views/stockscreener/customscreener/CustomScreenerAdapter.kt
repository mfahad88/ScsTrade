package com.example.scstrade.views.stockscreener.customscreener

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemStockScreenerBinding

import com.example.scstrade.databinding.ItemStockScreenerRowBinding
import com.example.scstrade.model.response.stockscreener.StockScreenerItem
import kotlin.reflect.full.memberProperties

class CustomScreenerAdapter(private val itemList: MutableList<StockScreenerItem>?, private val onItemClick: (StockScreenerItem) -> Unit) : RecyclerView.Adapter<CustomScreenerAdapter.CustomScreenerViewHolder>() {

    class CustomScreenerViewHolder(private val binding: ItemStockScreenerRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StockScreenerItem?, position: Int, onItemClick: (StockScreenerItem) -> Unit) {
            binding.stickyColumn.text = item?.symbol
            val staticKeys = setOf(
                "Symbol"
            )
            StockScreenerItem::class.memberProperties.forEach { prop ->
                if (prop.name !in staticKeys) {
                    val rawValue = item?.let { prop.get(it) }
                    val childBinding = ItemStockScreenerBinding.inflate(LayoutInflater.from(binding.root.context))
                    val valueStr = when {
                        rawValue == null -> ""
                        rawValue.toString().equals("null", ignoreCase = true) -> ""
                        rawValue is String -> rawValue.trim()
                        rawValue is Number -> rawValue.toString()
                        else -> rawValue.toString().trim()
                    }

                    if (valueStr.isNotEmpty()) {
                        childBinding.key.text = prop.name
                        if(position!=0){
                            childBinding.key.visibility = View.GONE
                        }
                        childBinding.value.text = valueStr
                        /*val row = LinearLayout(root.context).apply {
                            orientation = LinearLayout.HORIZONTAL
                        }

                        val keyTv = TextView(ContextThemeWrapper(root.context, R.style.engineering)).apply {
                            text =  prop.name
                                .replace("_", " ")
                                .replace(Regex("(?<=[a-z])(?=[A-Z])"), " ")
                                .replaceFirstChar { it.uppercaseChar() } + ": "
                            setTypeface(typeface, Typeface.BOLD)
                        }

                        val valueTv = TextView(root.context).apply {
                            text = valueStr
                        }*/


                        binding.horizontalContent.addView(childBinding.root)
                    }
                }
            }
            binding.root.setOnClickListener {
                if (item != null) {
                    onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomScreenerViewHolder {
        val binding = ItemStockScreenerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomScreenerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomScreenerViewHolder, position: Int) {
        holder.bind(itemList?.get(position),position, onItemClick)
    }

    override fun getItemCount(): Int {
        return itemList?.size?:0
    }

}
