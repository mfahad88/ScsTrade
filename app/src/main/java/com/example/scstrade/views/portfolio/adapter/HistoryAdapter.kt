package com.example.scstrade.views.portfolio.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemHistoryBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.portfolio.CloseTrade
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Collections
import java.util.Date
import kotlin.time.times

class HistoryAdapter(private val itemList: List<CloseTrade>, private val onItemClick: (CloseTrade) -> Unit) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CloseTrade, onItemClick: (CloseTrade) -> Unit) {
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 val formatter=  DateTimeFormatter.ofPattern("M/dd/yyyy")
                 val date = LocalDate.parse(item.salDate, formatter)
                 binding.sellDateValue.text = "${date.dayOfMonth}-${date.month.name.substring(0,3)}-${date.year.toString().substring(2,4)}"
            } else {

            }
            binding.sellPriceValue.text = Utils.roundTwoDecimal(item.salPrice.toDouble())

            binding.purchasePrValue.text = Utils.roundTwoDecimal(item.purPrice.toDouble())
            binding.netPriceValue.text = Utils.roundTwoDecimal(item.purAmount.toDouble())
            binding.netCostValue.text = Utils.roundTwoDecimal(item.salAmount.toDouble())
            binding.quantitySoValue.text = item.salQuantity
            binding.pLOnSellValue.text = "${Utils.roundTwoDecimal((item.salAmount.toDouble() - item.purAmount.toDouble()))}(${Utils.roundTwoDecimal(((item.salAmount.toDouble() - item.purAmount.toDouble()).div(item.salAmount.toDouble())).times(100))}%)"
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(itemList[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        Collections.swap(itemList, fromPosition, toPosition)
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
